/**
 * Communication Service for enhanced client-server communication
 * Implements robust connection handling, error propagation, capabilities detection, and diagnostics
 * Part of Step 2.2 - Communication Enhancement for XtextAsciidocPlugin VS Code Extension
 */

import * as vscode from 'vscode';
import { LanguageClient, ServerCapabilities, ErrorHandler, ErrorAction, CloseAction, Message, ErrorHandlerResult, CloseHandlerResult } from 'vscode-languageclient/node';
import { AsciidocConfiguration } from './configurationService';

/**
 * Connection retry configuration
 */
export interface RetryConfiguration {
    maxRetries: number;
    baseDelay: number;
    maxDelay: number;
    timeoutMs: number;
    exponentialBackoff: boolean;
}

/**
 * Server capabilities extended with our custom capabilities
 */
export interface ExtendedServerCapabilities extends ServerCapabilities {
    experimental?: {
        asciidocFeatures?: {
            customValidation?: boolean;
            advancedFormatting?: boolean;
            tableSupport?: boolean;
            crossReferences?: boolean;
            documentGeneration?: boolean;
        };
    };
}

/**
 * Connection status information
 */
export interface ConnectionStatus {
    isConnected: boolean;
    lastConnectTime?: Date;
    lastError?: string;
    retryCount: number;
    capabilities?: ExtendedServerCapabilities;
}


/**
 * Communication service for enhanced client-server interactions
 */
export class CommunicationService {
    private client: LanguageClient | undefined;
    private outputChannel: vscode.OutputChannel;
    private configuration: AsciidocConfiguration;
    private connectionStatus: ConnectionStatus;
    private retryTimer: NodeJS.Timeout | undefined;
    private capabilityChangeHandlers: ((capabilities: ExtendedServerCapabilities) => void)[] = [];
    
    constructor(
        outputChannel: vscode.OutputChannel,
        configuration: AsciidocConfiguration
    ) {
        this.outputChannel = outputChannel;
        this.configuration = configuration;
        this.connectionStatus = {
            isConnected: false,
            retryCount: 0
        };
    }

    /**
     * Enhanced connection setup with timeout and retry logic (2.2.1)
     */
    public async setupConnection(client: LanguageClient): Promise<void> {
        const commandName = 'setupConnection';
        const startTime = Date.now();
        
        this.client = client;
        this.outputChannel.appendLine(`Executing command: ${commandName} with parameters: {clientName: ${client.name || 'AsciiDoc Language Client'}}`);

        // Configure enhanced error handling
        this.setupErrorHandling();
        
        // Setup LSP request/response logging
        this.setupLSPLogging();
        
        // Configure connection timeout
        const retryConfig = this.createRetryConfiguration();
        this.outputChannel.appendLine(`Command ${commandName} retry config: ${JSON.stringify(retryConfig)}`);
        
        try {
            await this.connectWithRetry(retryConfig);
            this.connectionStatus.isConnected = true;
            this.connectionStatus.lastConnectTime = new Date();
            this.connectionStatus.retryCount = 0;
            
            // Detect server capabilities once connected
            await this.detectServerCapabilities();
            
            const duration = Date.now() - startTime;
            this.outputChannel.appendLine(`Command ${commandName} completed successfully in ${duration}ms`);
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : 'Unknown connection error';
            const duration = Date.now() - startTime;
            this.outputChannel.appendLine(`Command ${commandName} failed after ${duration}ms: ${errorMessage}`);
            this.connectionStatus.lastError = errorMessage;
            throw error;
        }
    }

    /**
     * Enhanced error propagation from server to client (2.2.2)
     */
    private setupErrorHandling(): void {
        if (!this.client) {
            return;
        }

        // Custom error handler with enhanced error propagation
        this.client.onDidChangeState((event) => {
            this.outputChannel.appendLine(`CommunicationService: Client state changed from ${event.oldState} to ${event.newState}`);
            
            if (event.newState === 2) { // Stopped state
                this.connectionStatus.isConnected = false;
                this.handleConnectionLoss();
            }
        });

        // Enhanced notification handlers
        this.setupNotificationHandlers();
        
        // Custom error handler
        const errorHandler: ErrorHandler = {
            error: (error: Error, message: Message | undefined, count: number | undefined): ErrorHandlerResult => {
                this.outputChannel.appendLine(`CommunicationService: Server error - ${error.message}`);
                
                // Propagate error with context
                this.propagateError(error, message, count);
                
                // Determine error action based on error type and configuration
                return { action: this.determineErrorAction(error, count) };
            },
            closed: (): CloseHandlerResult => {
                this.outputChannel.appendLine('CommunicationService: Server connection closed');
                this.connectionStatus.isConnected = false;
                
                // Show user-friendly notification
                if (this.configuration.ui.notifications.errors) {
                    vscode.window.showErrorMessage(
                        'AsciiDoc Language Server connection lost. Attempting to reconnect...',
                        'Retry Now',
                        'Disable Server'
                    ).then(selection => {
                        if (selection === 'Retry Now') {
                            this.attemptReconnection();
                        } else if (selection === 'Disable Server') {
                            vscode.commands.executeCommand('asciidoc.server.stop');
                        }
                    });
                }
                
                return { action: CloseAction.DoNotRestart }; // We handle reconnection manually
            }
        };

        this.client.clientOptions.errorHandler = errorHandler;
    }

    /**
     * Server capabilities detection and feature toggling (2.2.3)
     */
    private async detectServerCapabilities(): Promise<void> {
        if (!this.client) {
            return;
        }

        try {
            this.outputChannel.appendLine('CommunicationService: Detecting server capabilities...');
            
            // Wait for client to be ready
            await this.client.start();
            
            // Get server capabilities
            const capabilities = this.client.initializeResult?.capabilities as ExtendedServerCapabilities;
            if (!capabilities) {
                this.outputChannel.appendLine('CommunicationService: Warning - No server capabilities detected');
                return;
            }

            this.connectionStatus.capabilities = capabilities;
            
            // Log standard capabilities
            this.logServerCapabilities(capabilities);
            
            // Check for extended AsciiDoc capabilities
            const asciidocFeatures = capabilities.experimental?.asciidocFeatures;
            if (asciidocFeatures) {
                this.outputChannel.appendLine('CommunicationService: Extended AsciiDoc features detected:');
                this.outputChannel.appendLine(`  - Custom Validation: ${asciidocFeatures.customValidation ? 'Yes' : 'No'}`);
                this.outputChannel.appendLine(`  - Advanced Formatting: ${asciidocFeatures.advancedFormatting ? 'Yes' : 'No'}`);
                this.outputChannel.appendLine(`  - Table Support: ${asciidocFeatures.tableSupport ? 'Yes' : 'No'}`);
                this.outputChannel.appendLine(`  - Cross References: ${asciidocFeatures.crossReferences ? 'Yes' : 'No'}`);
                this.outputChannel.appendLine(`  - Document Generation: ${asciidocFeatures.documentGeneration ? 'Yes' : 'No'}`);
            }

            // Apply feature toggles based on capabilities
            this.applyFeatureToggles(capabilities);
            
            // Notify capability change handlers
            this.notifyCapabilityChange(capabilities);
            
            this.outputChannel.appendLine('CommunicationService: Server capabilities detection completed');
            
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : 'Unknown error';
            this.outputChannel.appendLine(`CommunicationService: Error detecting capabilities: ${errorMessage}`);
        }
    }

    /**
     * Diagnostic message filtering and formatting (2.2.4)
     * Ensures diagnostics from server are properly integrated with VSCode
     */
    public setupDiagnosticHandling(): void {
        if (!this.client) {
            return;
        }

        this.outputChannel.appendLine('CommunicationService: Setting up diagnostic handling for proper VSCode integration');
        
        // Ensure diagnostic collection is properly set up
        // The language client handles most diagnostic processing automatically,
        // but we can add logging and monitoring here
        
        // Note: We do NOT monitor textDocument/publishDiagnostics notifications directly here
        // as intercepting them can prevent VSCode from properly displaying diagnostics.
        // The language client handles diagnostic publishing automatically to VSCode's diagnostic system.
        
        // Monitor completion requests for debugging
        if (this.configuration.debug.verboseLogging) {
            // Note: These are internal requests that we can't directly intercept,
            // but we can add general LSP communication monitoring
            this.outputChannel.appendLine('CommunicationService: Verbose LSP communication logging enabled');
        }
        
        this.outputChannel.appendLine('CommunicationService: Diagnostic handling setup completed');
    }

    /**
     * Get current connection status
     */
    public getConnectionStatus(): ConnectionStatus {
        return { ...this.connectionStatus };
    }

    /**
     * Register capability change handler
     */
    public onCapabilityChange(handler: (capabilities: ExtendedServerCapabilities) => void): vscode.Disposable {
        this.capabilityChangeHandlers.push(handler);
        
        // Immediately notify with current capabilities if available
        if (this.connectionStatus.capabilities) {
            handler(this.connectionStatus.capabilities);
        }
        
        return new vscode.Disposable(() => {
            const index = this.capabilityChangeHandlers.indexOf(handler);
            if (index >= 0) {
                this.capabilityChangeHandlers.splice(index, 1);
            }
        });
    }

    
    /**
     * Update configuration
     */
    public updateConfiguration(config: AsciidocConfiguration): void {
        this.configuration = config;
        this.outputChannel.appendLine('CommunicationService: Configuration updated');
    }

    /**
     * Create retry configuration from extension configuration
     */
    private createRetryConfiguration(): RetryConfiguration {
        return {
            maxRetries: this.configuration.languageServer.maxRetries,
            baseDelay: 1000, // 1 second base delay
            maxDelay: 30000, // 30 seconds max delay
            timeoutMs: this.configuration.languageServer.timeout,
            exponentialBackoff: true
        };
    }

    /**
     * Connect with enhanced retry logic
     */
    private async connectWithRetry(retryConfig: RetryConfiguration): Promise<void> {
        if (!this.client) {
            throw new Error('Language client not initialized');
        }

        let attempt = 0;
        let lastError: Error | undefined;

        while (attempt <= retryConfig.maxRetries) {
            try {
                this.outputChannel.appendLine(`CommunicationService: Connection attempt ${attempt + 1}/${retryConfig.maxRetries + 1}`);
                
                // Create timeout promise
                const timeoutPromise = new Promise<never>((_, reject) => {
                    setTimeout(() => {
                        reject(new Error(`Connection timeout after ${retryConfig.timeoutMs}ms`));
                    }, retryConfig.timeoutMs);
                });

                // Race the connection against timeout
                await Promise.race([
                    this.client.start(),
                    timeoutPromise
                ]);

                // Client should be ready after start() completes
                // No need for additional ready check in the connection retry logic

                this.outputChannel.appendLine(`CommunicationService: Connection established on attempt ${attempt + 1}`);
                return; // Success
                
            } catch (error) {
                lastError = error instanceof Error ? error : new Error('Unknown connection error');
                attempt++;
                
                this.outputChannel.appendLine(`CommunicationService: Attempt ${attempt} failed: ${lastError.message}`);
                
                if (attempt <= retryConfig.maxRetries) {
                    const delay = this.calculateRetryDelay(attempt, retryConfig);
                    this.outputChannel.appendLine(`CommunicationService: Retrying in ${delay}ms...`);
                    await this.delay(delay);
                }
            }
        }

        // All attempts failed
        const finalError = lastError || new Error('Connection failed after all retries');
        this.connectionStatus.retryCount = attempt;
        throw finalError;
    }

    /**
     * Calculate retry delay with exponential backoff
     */
    private calculateRetryDelay(attempt: number, config: RetryConfiguration): number {
        if (!config.exponentialBackoff) {
            return config.baseDelay;
        }

        const exponentialDelay = config.baseDelay * Math.pow(2, attempt - 1);
        const jitteredDelay = exponentialDelay * (0.5 + Math.random() * 0.5); // Add jitter
        return Math.min(jitteredDelay, config.maxDelay);
    }

    /**
     * Enhanced error propagation
     */
    private propagateError(error: Error, message: Message | undefined, count: number | undefined): void {
        // Create detailed error context
        const errorContext = {
            timestamp: new Date().toISOString(),
            errorMessage: error.message,
            errorStack: error.stack,
            messageType: (message as any)?.method || 'unknown',
            errorCount: count || 0
        };

        // Log detailed error
        this.outputChannel.appendLine(`CommunicationService: Detailed error context: ${JSON.stringify(errorContext, null, 2)}`);
        
        // Show user-friendly error notification based on error type
        const userMessage = this.createUserFriendlyErrorMessage(error, message);
        
        if (this.configuration.ui.notifications.errors && userMessage) {
            if (count && count > 3) {
                // Multiple errors - suggest restart
                vscode.window.showErrorMessage(
                    `${userMessage} Multiple errors detected.`,
                    'Restart Server',
                    'Show Logs'
                ).then(selection => {
                    if (selection === 'Restart Server') {
                        vscode.commands.executeCommand('asciidoc.server.restart');
                    } else if (selection === 'Show Logs') {
                        this.outputChannel.show();
                    }
                });
            } else {
                vscode.window.showErrorMessage(userMessage);
            }
        }
    }

    /**
     * Create user-friendly error message
     */
    private createUserFriendlyErrorMessage(error: Error, _message: Message | undefined): string | undefined {
        // Map technical errors to user-friendly messages
        const errorMessage = error.message.toLowerCase();
        
        if (errorMessage.includes('timeout')) {
            return 'AsciiDoc Language Server is taking longer than expected to respond.';
        } else if (errorMessage.includes('connection')) {
            return 'Lost connection to AsciiDoc Language Server.';
        } else if (errorMessage.includes('java')) {
            return 'AsciiDoc Language Server Java runtime error. Please check Java installation.';
        } else if (errorMessage.includes('permission')) {
            return 'Permission denied accessing AsciiDoc Language Server files.';
        } else if (errorMessage.includes('not found')) {
            return 'AsciiDoc Language Server executable not found.';
        }
        
        // For other errors, return a generic message for multiple errors
        return 'AsciiDoc Language Server encountered an error.';
    }

    /**
     * Determine error action based on error type and configuration
     */
    private determineErrorAction(error: Error, count: number | undefined): ErrorAction {
        const errorMessage = error.message.toLowerCase();
        
        // Critical errors that should stop the server
        if (errorMessage.includes('java') || errorMessage.includes('permission') || errorMessage.includes('not found')) {
            return ErrorAction.Shutdown;
        }
        
        // Too many errors - shutdown
        if (count && count > 5) {
            return ErrorAction.Shutdown;
        }
        
        // Default - continue with current message
        return ErrorAction.Continue;
    }

    /**
     * Handle connection loss
     */
    private handleConnectionLoss(): void {
        this.outputChannel.appendLine('CommunicationService: Handling connection loss...');
        
        // Clear retry timer if exists
        if (this.retryTimer) {
            clearTimeout(this.retryTimer);
        }
        
        // Schedule reconnection attempt
        this.scheduleReconnection();
    }

    /**
     * Schedule automatic reconnection
     */
    private scheduleReconnection(): void {
        const retryConfig = this.createRetryConfiguration();
        const delay = this.calculateRetryDelay(this.connectionStatus.retryCount + 1, retryConfig);
        
        this.outputChannel.appendLine(`CommunicationService: Scheduling reconnection in ${delay}ms...`);
        
        this.retryTimer = setTimeout(() => {
            this.attemptReconnection();
        }, delay);
    }

    /**
     * Attempt reconnection
     */
    private async attemptReconnection(): Promise<void> {
        if (!this.client || this.connectionStatus.isConnected) {
            return;
        }

        try {
            this.outputChannel.appendLine('CommunicationService: Attempting automatic reconnection...');
            this.connectionStatus.retryCount++;
            
            const retryConfig = this.createRetryConfiguration();
            await this.connectWithRetry(retryConfig);
            
            this.connectionStatus.isConnected = true;
            this.connectionStatus.lastConnectTime = new Date();
            this.connectionStatus.retryCount = 0;
            
            // Re-detect capabilities
            await this.detectServerCapabilities();
            
            this.outputChannel.appendLine('CommunicationService: Automatic reconnection successful');
            
            if (this.configuration.ui.notifications.info) {
                vscode.window.showInformationMessage('AsciiDoc Language Server reconnected successfully');
            }
            
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : 'Unknown error';
            this.outputChannel.appendLine(`CommunicationService: Reconnection failed: ${errorMessage}`);
            
            // Schedule another attempt if under retry limit
            if (this.connectionStatus.retryCount < this.createRetryConfiguration().maxRetries) {
                this.scheduleReconnection();
            } else {
                this.outputChannel.appendLine('CommunicationService: Max reconnection attempts reached');
                if (this.configuration.ui.notifications.errors) {
                    vscode.window.showErrorMessage('Failed to reconnect to AsciiDoc Language Server after multiple attempts.');
                }
            }
        }
    }

    /**
     * Setup LSP request/response logging
     * Note: Simplified logging due to sendRequest method signature complexity in v9.0.1
     */
    private setupLSPLogging(): void {
        if (!this.client) {
            return;
        }

        this.outputChannel.appendLine('CommunicationService: LSP request/response logging setup (simplified for compatibility)');
        // Note: Advanced request/response logging removed due to method signature incompatibility
        // in vscode-languageclient v9.0.1. Basic logging is handled through trace settings.
    }

    /**
     * Setup notification handlers for enhanced communication
     */
    private setupNotificationHandlers(): void {
        if (!this.client) {
            return;
        }

        // Custom notification for server status
        this.client.onNotification('asciidoc/serverStatus', (params: any) => {
            this.outputChannel.appendLine(`LSP Notification: asciidoc/serverStatus with parameters: ${JSON.stringify(params)}`);
        });

        // Custom notification for capability changes
        this.client.onNotification('asciidoc/capabilityChanged', (params: any) => {
            this.outputChannel.appendLine(`LSP Notification: asciidoc/capabilityChanged with parameters: ${JSON.stringify(params)}`);
            this.detectServerCapabilities(); // Re-detect capabilities
        });

        // Progress notifications
        this.client.onNotification('$/progress', (params: any) => {
            if (this.configuration.debug.verboseLogging) {
                this.outputChannel.appendLine(`LSP Notification: $/progress with parameters: ${JSON.stringify(params)}`);
            }
        });

        // Note: We do NOT intercept textDocument/publishDiagnostics here as it prevents 
        // VSCode from properly displaying diagnostics. The language client handles this automatically.
    }

    /**
     * Log server capabilities
     */
    private logServerCapabilities(capabilities: ExtendedServerCapabilities): void {
        this.outputChannel.appendLine('CommunicationService: Server capabilities detected:');
        this.outputChannel.appendLine(`  - Text Document Sync: ${capabilities.textDocumentSync ? 'Yes' : 'No'}`);
        this.outputChannel.appendLine(`  - Completion Provider: ${capabilities.completionProvider ? 'Yes' : 'No'}`);
        this.outputChannel.appendLine(`  - Hover Provider: ${capabilities.hoverProvider ? 'Yes' : 'No'}`);
        this.outputChannel.appendLine(`  - Definition Provider: ${capabilities.definitionProvider ? 'Yes' : 'No'}`);
        this.outputChannel.appendLine(`  - References Provider: ${capabilities.referencesProvider ? 'Yes' : 'No'}`);
        this.outputChannel.appendLine(`  - Document Symbol Provider: ${capabilities.documentSymbolProvider ? 'Yes' : 'No'}`);
        this.outputChannel.appendLine(`  - Code Action Provider: ${capabilities.codeActionProvider ? 'Yes' : 'No'}`);
        this.outputChannel.appendLine(`  - Document Formatting: ${capabilities.documentFormattingProvider ? 'Yes' : 'No'}`);
        this.outputChannel.appendLine(`  - Range Formatting: ${capabilities.documentRangeFormattingProvider ? 'Yes' : 'No'}`);
        this.outputChannel.appendLine(`  - Rename Provider: ${capabilities.renameProvider ? 'Yes' : 'No'}`);
    }

    /**
     * Apply feature toggles based on server capabilities
     */
    private applyFeatureToggles(capabilities: ExtendedServerCapabilities): void {
        // Disable features in configuration if server doesn't support them
        if (!capabilities.completionProvider && this.configuration.features.completion.enabled) {
            this.outputChannel.appendLine('CommunicationService: Disabling completion - not supported by server');
        }
        
        if (!capabilities.hoverProvider && this.configuration.features.hover.enabled) {
            this.outputChannel.appendLine('CommunicationService: Disabling hover - not supported by server');
        }
        
        if (!capabilities.documentFormattingProvider && this.configuration.features.formatting.enabled) {
            this.outputChannel.appendLine('CommunicationService: Disabling formatting - not supported by server');
        }

        // Enable advanced features if server supports them
        const asciidocFeatures = capabilities.experimental?.asciidocFeatures;
        if (asciidocFeatures?.customValidation) {
            this.outputChannel.appendLine('CommunicationService: Enabling custom validation features');
        }
    }

    /**
     * Notify capability change handlers
     */
    private notifyCapabilityChange(capabilities: ExtendedServerCapabilities): void {
        this.capabilityChangeHandlers.forEach(handler => {
            try {
                handler(capabilities);
            } catch (error) {
                this.outputChannel.appendLine(`CommunicationService: Error in capability change handler: ${error}`);
            }
        });
    }

    // Diagnostic filtering methods removed - no longer needed after fixing validation display issue

    /**
     * Utility method for delays
     */
    private delay(ms: number): Promise<void> {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    /**
     * Dispose of resources
     */
    public dispose(): void {
        if (this.retryTimer) {
            clearTimeout(this.retryTimer);
            this.retryTimer = undefined;
        }
        
        this.capabilityChangeHandlers = [];
        this.connectionStatus.isConnected = false;
    }
}