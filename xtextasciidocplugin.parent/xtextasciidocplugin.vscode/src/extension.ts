'use strict';

import * as vscode from 'vscode';
import * as os from 'os';
import { Trace } from 'vscode-jsonrpc';
import { LanguageClient, LanguageClientOptions } from 'vscode-languageclient/node';
import * as constants from './constants';
import { ConfigurationService, AsciidocConfiguration, ConfigurationChangeEvent } from './configurationService';
import { ServerLauncher, ServerStatus } from './serverLauncher';
import { ExtendedServerCapabilities } from './communicationService';

// Global extension state
let serverLauncher: ServerLauncher | undefined;
let outputChannel: vscode.OutputChannel | undefined;
let statusBarItem: vscode.StatusBarItem | undefined;
let configurationService: ConfigurationService | undefined;

// Legacy compatibility
let client: LanguageClient | undefined;

/**
 * Extension activation function
 * Called when the extension is activated
 */
export function activate(context: vscode.ExtensionContext): void {
    outputChannel = vscode.window.createOutputChannel(constants.OUTPUT_CHANNELS.EXTENSION);
    outputChannel.appendLine('Xtext AsciiDoc Extension: Initializing...');
    context.subscriptions.push(outputChannel);

    // Initialize configuration service
    configurationService = new ConfigurationService(outputChannel);
    context.subscriptions.push(configurationService);
    
    // Get current configuration
    const configuration = configurationService.getConfiguration();
    
    // Check if extension is enabled
    if (!configuration.languageServer.enabled) {
        outputChannel.appendLine('Xtext AsciiDoc language server is disabled in settings');
        return;
    }

    outputChannel.appendLine(`Platform: ${os.platform()}`);
    outputChannel.appendLine(`Configuration loaded: ${JSON.stringify(configuration, null, 2)}`);

    try {
        // Setup configuration change handlers
        setupConfigurationChangeHandlers(context);
        
        // Initialize server launcher
        initializeServerLauncher(context, configuration);
        
        // Register commands
        registerCommands(context);
        
        // Setup status bar
        setupStatusBar(context, configuration);
        
        outputChannel.appendLine('Xtext AsciiDoc Extension: Activation completed successfully');
    } catch (error) {
        const errorMessage = error instanceof Error ? error.message : 'Unknown error';
        outputChannel.appendLine(`Xtext AsciiDoc Extension: Failed to activate: ${errorMessage}`);
        
        // Show error notification based on configuration
        if (configuration.ui.notifications.errors) {
            vscode.window.showErrorMessage(`Failed to activate Xtext AsciiDoc Extension: ${errorMessage}`);
        }
    }
}

/**
 * Extension deactivation function
 * Called when the extension is being deactivated
 */
export function deactivate(): Thenable<void> | undefined {
    outputChannel?.appendLine('Xtext AsciiDoc Extension: Deactivating...');
    
    // Clean up status bar
    if (statusBarItem) {
        statusBarItem.dispose();
        statusBarItem = undefined;
    }
    
    // Clean up configuration service
    if (configurationService) {
        configurationService.dispose();
        configurationService = undefined;
    }
    
    // Stop server launcher gracefully
    if (serverLauncher) {
        return serverLauncher.stopServer().then(() => {
            serverLauncher?.dispose();
            serverLauncher = undefined;
            client = undefined;
            outputChannel?.appendLine('Xtext AsciiDoc Extension: Deactivated successfully');
        }).catch((error) => {
            const errorMessage = error instanceof Error ? error.message : 'Unknown error';
            outputChannel?.appendLine(`Xtext AsciiDoc Extension: Error during deactivation: ${errorMessage}`);
        });
    }
    
    return undefined;
}

/**
 * Initialize the server launcher with production-ready capabilities
 */
function initializeServerLauncher(context: vscode.ExtensionContext, configuration: AsciidocConfiguration): void {
    outputChannel?.appendLine(`Log directory: ${configuration.logDirectory}`);
    outputChannel?.appendLine(`Debug mode: ${configuration.debug.enabled}`);
    outputChannel?.appendLine(`Server port: ${configuration.languageServer.port}`);
    outputChannel?.appendLine(`Server timeout: ${configuration.languageServer.timeout}ms`);
    outputChannel?.appendLine(`Max retries: ${configuration.languageServer.maxRetries}`);

    // Create server launcher
    serverLauncher = new ServerLauncher(context, configuration, outputChannel!, statusBarItem);
    
    // Configure client options
    const clientOptions: LanguageClientOptions = createClientOptions(configuration);

    // Configure tracing
    const trace = getTraceLevel(configuration.trace.server || configuration.trace.level);

    // Start the server using the new launcher
    serverLauncher.startServer(clientOptions).then(() => {
        client = serverLauncher?.getClient();
        
        if (client) {
            client.setTrace(trace);
            context.subscriptions.push(client);
        }
        
        outputChannel?.appendLine('Xtext AsciiDoc language server started successfully using ServerLauncher');
    }).catch(error => {
        const errorMessage = error instanceof Error ? error.message : 'Unknown error';
        outputChannel?.appendLine(`Failed to start Xtext AsciiDoc language server: ${errorMessage}`);
        
        if (configuration.ui.notifications.errors) {
            vscode.window.showErrorMessage(`Failed to start Xtext AsciiDoc language server: ${errorMessage}`);
        }
    });

    context.subscriptions.push({
        dispose: () => {
            serverLauncher?.dispose();
            serverLauncher = undefined;
        }
    });
}

/**
 * Create client options based on configuration
 */
function createClientOptions(configuration: AsciidocConfiguration): LanguageClientOptions {
    const clientOptions: LanguageClientOptions = {
        documentSelector: [{ scheme: 'file', language: constants.LANGUAGE_ID }],
        synchronize: {
            fileEvents: vscode.workspace.createFileSystemWatcher(constants.FILE_PATTERNS.ASCIIDOC_FILES)
        },
        outputChannel: vscode.window.createOutputChannel(constants.OUTPUT_CHANNELS.LANGUAGE_SERVER),
        traceOutputChannel: vscode.window.createOutputChannel(constants.OUTPUT_CHANNELS.TRACE)
    };

    // Add initialization options based on configuration
    clientOptions.initializationOptions = {
        features: configuration.features,
        performance: configuration.performance
    };

    return clientOptions;
}

// Note: startLanguageServerWithRetry function is now handled by ServerLauncher class

/**
 * Convert string trace level to VSCode trace enum
 */
function getTraceLevel(traceLevel: string): Trace {
    switch (traceLevel.toLowerCase()) {
        case 'off': return Trace.Off;
        case 'messages': return Trace.Messages;
        case 'verbose': return Trace.Verbose;
        default: return Trace.Messages;
    }
}

/**
 * Register extension commands including server management
 */
function registerCommands(context: vscode.ExtensionContext): void {
    
    // Register server management commands
    const restartServerCommand = vscode.commands.registerCommand('asciidoc.server.restart', async () => {
        const commandName = 'asciidoc.server.restart';
        const startTime = Date.now();
        outputChannel?.appendLine(`Executing command: ${commandName} with parameters: {}`);
        
        if (!serverLauncher) {
            const errorMsg = 'AsciiDoc Language Server is not running';
            outputChannel?.appendLine(`Command ${commandName} failed: ${errorMsg}`);
            vscode.window.showWarningMessage(errorMsg);
            return;
        }
        
        try {
            const configuration = configurationService?.getConfiguration();
            if (!configuration) {
                throw new Error('Configuration not available');
            }
            
            const clientOptions = createClientOptions(configuration);
            outputChannel?.appendLine(`Command ${commandName} parameters: {configuration: ${JSON.stringify({
                enabled: configuration.languageServer.enabled,
                port: configuration.languageServer.port,
                timeout: configuration.languageServer.timeout
            })}}`);
            
            await serverLauncher.restartServer(clientOptions);
            
            // Update client reference
            client = serverLauncher.getClient();
            
            const duration = Date.now() - startTime;
            outputChannel?.appendLine(`Command ${commandName} completed successfully in ${duration}ms`);
            vscode.window.showInformationMessage('AsciiDoc Language Server restarted successfully');
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : 'Unknown error';
            const duration = Date.now() - startTime;
            outputChannel?.appendLine(`Command ${commandName} failed after ${duration}ms: ${errorMessage}`);
            vscode.window.showErrorMessage(`Failed to restart AsciiDoc Language Server: ${errorMessage}`);
        }
    });
    
    const showServerHealthCommand = vscode.commands.registerCommand('asciidoc.server.showHealth', () => {
        const commandName = 'asciidoc.server.showHealth';
        const startTime = Date.now();
        outputChannel?.appendLine(`Executing command: ${commandName} with parameters: {}`);
        
        if (!serverLauncher) {
            const errorMsg = 'AsciiDoc Language Server is not running';
            outputChannel?.appendLine(`Command ${commandName} failed: ${errorMsg}`);
            vscode.window.showInformationMessage(errorMsg);
            return;
        }
        
        try {
            const healthInfo = serverLauncher.getHealthInfo();
            const connectionStatus = serverLauncher.getConnectionStatus();
            
            outputChannel?.appendLine(`Command ${commandName} retrieving health info: {status: ${healthInfo.status}, connected: ${connectionStatus.isConnected}}`);
            
            const uptimeStr = healthInfo.uptime ? `${Math.round(healthInfo.uptime / 1000)}s` : 'Unknown';
            const startTimeStr = healthInfo.startTime ? healthInfo.startTime.toLocaleString() : 'Unknown';
            const lastCheckStr = healthInfo.lastHealthCheck ? healthInfo.lastHealthCheck.toLocaleString() : 'Never';
            const connectTimeStr = connectionStatus.lastConnectTime ? connectionStatus.lastConnectTime.toLocaleString() : 'Never';
            
            const healthMessage = [
                `Status: ${healthInfo.status}`,
                `Connection: ${connectionStatus.isConnected ? 'Connected' : 'Disconnected'}`,
                `Start Time: ${startTimeStr}`,
                `Last Connect: ${connectTimeStr}`,
                `Uptime: ${uptimeStr}`,
                `Last Health Check: ${lastCheckStr}`,
                `Retry Count: ${connectionStatus.retryCount}`,
                healthInfo.pid ? `Process ID: ${healthInfo.pid}` : '',
                healthInfo.errorMessage ? `Error: ${healthInfo.errorMessage}` : '',
                connectionStatus.lastError ? `Connection Error: ${connectionStatus.lastError}` : ''
            ].filter(line => line).join('\n');
            
            const duration = Date.now() - startTime;
            outputChannel?.appendLine(`Command ${commandName} completed successfully in ${duration}ms`);
            vscode.window.showInformationMessage(`AsciiDoc Language Server Health:\n\n${healthMessage}`, { modal: true });
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : 'Unknown error';
            const duration = Date.now() - startTime;
            outputChannel?.appendLine(`Command ${commandName} failed after ${duration}ms: ${errorMessage}`);
        }
    });
    
    const stopServerCommand = vscode.commands.registerCommand('asciidoc.server.stop', async () => {
        const commandName = 'asciidoc.server.stop';
        const startTime = Date.now();
        outputChannel?.appendLine(`Executing command: ${commandName} with parameters: {}`);
        
        if (!serverLauncher) {
            const errorMsg = 'AsciiDoc Language Server is not running';
            outputChannel?.appendLine(`Command ${commandName} failed: ${errorMsg}`);
            vscode.window.showWarningMessage(errorMsg);
            return;
        }
        
        try {
            await serverLauncher.stopServer();
            client = undefined;
            
            const duration = Date.now() - startTime;
            outputChannel?.appendLine(`Command ${commandName} completed successfully in ${duration}ms`);
            vscode.window.showInformationMessage('AsciiDoc Language Server stopped');
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : 'Unknown error';
            const duration = Date.now() - startTime;
            outputChannel?.appendLine(`Command ${commandName} failed after ${duration}ms: ${errorMessage}`);
            vscode.window.showErrorMessage(`Failed to stop AsciiDoc Language Server: ${errorMessage}`);
        }
    });
    
    // Add command to show server capabilities
    const showCapabilitiesCommand = vscode.commands.registerCommand('asciidoc.server.showCapabilities', () => {
        const commandName = 'asciidoc.server.showCapabilities';
        const startTime = Date.now();
        outputChannel?.appendLine(`Executing command: ${commandName} with parameters: {}`);
        
        if (!serverLauncher) {
            const errorMsg = 'AsciiDoc Language Server is not running';
            outputChannel?.appendLine(`Command ${commandName} failed: ${errorMsg}`);
            vscode.window.showInformationMessage(errorMsg);
            return;
        }
        
        try {
            const connectionStatus = serverLauncher.getConnectionStatus();
            const capabilities = connectionStatus.capabilities;
            
            outputChannel?.appendLine(`Command ${commandName} retrieving capabilities: {hasCapabilities: ${!!capabilities}}`);
            
            if (!capabilities) {
                const warningMsg = 'Server capabilities not yet detected. Please wait for server to start.';
                outputChannel?.appendLine(`Command ${commandName} warning: ${warningMsg}`);
                vscode.window.showInformationMessage(warningMsg);
                return;
            }
            
            const capabilitiesInfo = [
                'Standard Language Server Features:',
                `• Text Document Sync: ${capabilities.textDocumentSync ? '✓' : '✗'}`,
                `• Completion Provider: ${capabilities.completionProvider ? '✓' : '✗'}`,
                `• Hover Provider: ${capabilities.hoverProvider ? '✓' : '✗'}`,
                `• Definition Provider: ${capabilities.definitionProvider ? '✓' : '✗'}`,
                `• References Provider: ${capabilities.referencesProvider ? '✓' : '✗'}`,
                `• Document Symbol Provider: ${capabilities.documentSymbolProvider ? '✓' : '✗'}`,
                `• Code Action Provider: ${capabilities.codeActionProvider ? '✓' : '✗'}`,
                `• Document Formatting: ${capabilities.documentFormattingProvider ? '✓' : '✗'}`,
                `• Range Formatting: ${capabilities.documentRangeFormattingProvider ? '✓' : '✗'}`,
                `• Rename Provider: ${capabilities.renameProvider ? '✓' : '✗'}`,
            ];
            
            const asciidocFeatures = capabilities.experimental?.asciidocFeatures;
            if (asciidocFeatures) {
                capabilitiesInfo.push(
                    '',
                    'Extended AsciiDoc Features:',
                    `• Custom Validation: ${asciidocFeatures.customValidation ? '✓' : '✗'}`,
                    `• Advanced Formatting: ${asciidocFeatures.advancedFormatting ? '✓' : '✗'}`,
                    `• Table Support: ${asciidocFeatures.tableSupport ? '✓' : '✗'}`,
                    `• Cross References: ${asciidocFeatures.crossReferences ? '✓' : '✗'}`,
                    `• Document Generation: ${asciidocFeatures.documentGeneration ? '✓' : '✗'}`
                );
            }
            
            const capabilitiesMessage = capabilitiesInfo.join('\n');
            const duration = Date.now() - startTime;
            outputChannel?.appendLine(`Command ${commandName} completed successfully in ${duration}ms`);
            vscode.window.showInformationMessage(`AsciiDoc Language Server Capabilities:\n\n${capabilitiesMessage}`, { modal: true });
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : 'Unknown error';
            const duration = Date.now() - startTime;
            outputChannel?.appendLine(`Command ${commandName} failed after ${duration}ms: ${errorMessage}`);
        }
    });
    
    const startServerCommand = vscode.commands.registerCommand('asciidoc.server.start', async () => {
        const commandName = 'asciidoc.server.start';
        const startTime = Date.now();
        outputChannel?.appendLine(`Executing command: ${commandName} with parameters: {}`);
        
        if (serverLauncher && serverLauncher.getHealthInfo().status === ServerStatus.RUNNING) {
            const warningMsg = 'AsciiDoc Language Server is already running';
            outputChannel?.appendLine(`Command ${commandName} warning: ${warningMsg}`);
            vscode.window.showWarningMessage(warningMsg);
            return;
        }
        
        try {
            const configuration = configurationService?.getConfiguration();
            if (!configuration) {
                throw new Error('Configuration not available');
            }
            
            outputChannel?.appendLine(`Command ${commandName} parameters: {configuration: ${JSON.stringify({
                enabled: configuration.languageServer.enabled,
                port: configuration.languageServer.port,
                timeout: configuration.languageServer.timeout
            })}}`);
            
            if (!serverLauncher) {
                // Reinitialize if needed
                initializeServerLauncher(context, configuration);
            } else {
                const clientOptions = createClientOptions(configuration);
                await serverLauncher.startServer(clientOptions);
                
                // Update client reference
                client = serverLauncher.getClient();
            }
            
            const duration = Date.now() - startTime;
            outputChannel?.appendLine(`Command ${commandName} completed successfully in ${duration}ms`);
            vscode.window.showInformationMessage('AsciiDoc Language Server started successfully');
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : 'Unknown error';
            const duration = Date.now() - startTime;
            outputChannel?.appendLine(`Command ${commandName} failed after ${duration}ms: ${errorMessage}`);
            vscode.window.showErrorMessage(`Failed to start AsciiDoc Language Server: ${errorMessage}`);
        }
    });
    
    // Add code generation command
    const generateCodeCommand = vscode.commands.registerCommand('asciidoc.generateCode', async () => {
        const commandName = 'asciidoc.generateCode';
        const startTime = Date.now();
        outputChannel?.appendLine(`Executing command: ${commandName} with parameters: {}`);
        
        const activeEditor = vscode.window.activeTextEditor;
        if (!activeEditor || activeEditor.document.languageId !== 'asciidoc') {
            const errorMsg = 'No active AsciiDoc document';
            outputChannel?.appendLine(`Command ${commandName} failed: ${errorMsg}`);
            vscode.window.showWarningMessage(errorMsg);
            return;
        }
        
        if (!serverLauncher) {
            const errorMsg = 'AsciiDoc Language Server is not running';
            outputChannel?.appendLine(`Command ${commandName} failed: ${errorMsg}`);
            vscode.window.showWarningMessage(errorMsg);
            return;
        }
        
        try {
            const client = serverLauncher.getClient();
            if (!client) {
                throw new Error('Language client not available');
            }
            
            const documentUri = activeEditor.document.uri.toString();
            outputChannel?.appendLine(`Command ${commandName} parameters: {documentUri: ${documentUri}, serverCommand: "asciidoc.generate"}`);
            
            const result = await client.sendRequest('workspace/executeCommand', {
                command: 'asciidoc.generate',
                arguments: [documentUri]
            });
            
            const duration = Date.now() - startTime;
            outputChannel?.appendLine(`Command ${commandName} completed successfully in ${duration}ms: ${result}`);
            vscode.window.showInformationMessage(`Code generation result: ${result}`);
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : 'Unknown error';
            const duration = Date.now() - startTime;
            outputChannel?.appendLine(`Command ${commandName} failed after ${duration}ms: ${errorMessage}`);
            vscode.window.showErrorMessage(`Code generation failed: ${errorMessage}`);
        }
    });
    
    // Add logging management commands
    const setLoggingLevelCommand = vscode.commands.registerCommand('asciidoc.logging.setLevel', async () => {
        const levels = ['OFF', 'ERROR', 'WARN', 'INFO', 'DEBUG', 'TRACE'];
        const selected = await vscode.window.showQuickPick(levels, {
            placeHolder: 'Select logging level'
        });
        if (selected) {
            await vscode.workspace.getConfiguration().update('asciidoc.logging.level', selected, vscode.ConfigurationTarget.Workspace);
            outputChannel?.appendLine(`Logging level changed to: ${selected}`);
            vscode.window.showInformationMessage(`AsciiDoc logging level set to ${selected}`);
        }
    });
    
    const showOutputCommand = vscode.commands.registerCommand('asciidoc.logging.showOutput', () => {
        outputChannel?.show();
    });
    
    const showServerOutputCommand = vscode.commands.registerCommand('asciidoc.logging.showServerOutput', () => {
        const serverChannel = vscode.window.createOutputChannel(constants.OUTPUT_CHANNELS.LANGUAGE_SERVER);
        serverChannel.show();
    });
    
    const clearLogsCommand = vscode.commands.registerCommand('asciidoc.logging.clearLogs', () => {
        outputChannel?.clear();
        outputChannel?.appendLine('Extension logs cleared');
        vscode.window.showInformationMessage('AsciiDoc extension logs cleared');
    });
    
    const exportLogsCommand = vscode.commands.registerCommand('asciidoc.logging.exportLogs', async () => {
        const workspaceFolder = vscode.workspace.workspaceFolders?.[0];
        const defaultPath = workspaceFolder?.uri.fsPath || require('os').homedir();
        const uri = await vscode.window.showSaveDialog({
            defaultUri: vscode.Uri.file(`${defaultPath}/asciidoc-extension-logs.txt`),
            filters: {
                'Text files': ['txt'],
                'All files': ['*']
            }
        });
        
        if (uri) {
            // This is a simplified export - in reality you'd want to collect actual log entries
            const logContent = `AsciiDoc Extension Logs - ${new Date().toISOString()}\n` +
                              `====================================================\n\n` +
                              `Configuration:\n${JSON.stringify(configurationService?.getConfiguration(), null, 2)}\n\n` +
                              `Note: Detailed log export functionality would be implemented here.\n`;
            
            await vscode.workspace.fs.writeFile(uri, Buffer.from(logContent));
            vscode.window.showInformationMessage(`Logs exported to ${uri.fsPath}`);
        }
    });

    context.subscriptions.push(
        restartServerCommand,
        showServerHealthCommand,
        showCapabilitiesCommand,
        stopServerCommand,
        startServerCommand,
        generateCodeCommand,
        setLoggingLevelCommand,
        showOutputCommand,
        showServerOutputCommand,
        clearLogsCommand,
        exportLogsCommand
    );
    
    outputChannel?.appendLine('Commands registered successfully (including enhanced server management)');
    
    // Setup capability change handler if server launcher exists
    if (serverLauncher) {
        const communicationService = serverLauncher.getCommunicationService();
        communicationService.onCapabilityChange((_capabilities: ExtendedServerCapabilities) => {
            outputChannel?.appendLine('Extension: Server capabilities updated');
            
            // Update status bar with capability information if desired
            if (statusBarItem && configurationService) {
                const config = configurationService.getConfiguration();
                updateStatusBar(true, config);
            }
        });
    }
}

/**
 * Setup configuration change handlers
 */
function setupConfigurationChangeHandlers(context: vscode.ExtensionContext): void {
    if (!configurationService) {
        return;
    }
    
    const configChangeHandler = configurationService.onConfigurationChange(async (event: ConfigurationChangeEvent) => {
        outputChannel?.appendLine(`Configuration change detected: ${event.changed.join(', ')}`);
        
        // Handle UI changes immediately
        if (event.changed.some(key => key.startsWith('ui.'))) {
            const currentConfig = configurationService?.getConfiguration();
            if (currentConfig && statusBarItem && serverLauncher) {
                // Status bar is now managed by ServerLauncher
                const healthInfo = serverLauncher.getHealthInfo();
                const isRunning = healthInfo.status === ServerStatus.RUNNING;
                updateStatusBar(isRunning, currentConfig);
            }
        }
        
        // Handle language server changes
        if (event.affectsLanguageServer) {
            if (event.requiresRestart && event.newConfig.languageServer.restartOnConfigChange) {
                outputChannel?.appendLine('Configuration change requires language server restart');
                
                if (event.newConfig.ui.notifications.info) {
                    vscode.window.showInformationMessage('Restarting AsciiDoc language server due to configuration changes...');
                }
                
                try {
                    if (serverLauncher) {
                        await restartServerLauncher(context, event.newConfig);
                    }
                } catch (error) {
                    const errorMessage = error instanceof Error ? error.message : 'Unknown error';
                    outputChannel?.appendLine(`Failed to restart language server: ${errorMessage}`);
                    
                    if (event.newConfig.ui.notifications.errors) {
                        vscode.window.showErrorMessage(`Failed to restart AsciiDoc language server: ${errorMessage}`);
                    }
                }
            } else {
                outputChannel?.appendLine('Configuration change affects language server but restart not required or disabled');
            }
        }
    });
    
    context.subscriptions.push(configChangeHandler);
}

/**
 * Restart the server launcher with new configuration
 */
async function restartServerLauncher(context: vscode.ExtensionContext, configuration: AsciidocConfiguration): Promise<void> {
    if (serverLauncher) {
        outputChannel?.appendLine('Restarting server launcher with new configuration...');
        
        // Create client options for restart
        const clientOptions: LanguageClientOptions = createClientOptions(configuration);
        
        // Restart using the server launcher
        await serverLauncher.restartServer(clientOptions);
        
        // Update client reference
        client = serverLauncher.getClient();
        
        // Configure tracing
        if (client) {
            const trace = getTraceLevel(configuration.trace.server || configuration.trace.level);
            client.setTrace(trace);
        }
        
        outputChannel?.appendLine('Server launcher restarted successfully');
    } else {
        outputChannel?.appendLine('No server launcher to restart, initializing new one...');
        initializeServerLauncher(context, configuration);
    }
}

/**
 * Setup status bar item
 */
function setupStatusBar(context: vscode.ExtensionContext, configuration: AsciidocConfiguration): void {
    if (!configuration.ui.statusBar.enabled) {
        return; // Status bar is disabled
    }
    
    statusBarItem = vscode.window.createStatusBarItem(vscode.StatusBarAlignment.Right, constants.STATUS_BAR.PRIORITY);
    statusBarItem.tooltip = constants.STATUS_BAR.TOOLTIP;
    context.subscriptions.push(statusBarItem);
    
    updateStatusBar(false, configuration); // Initially not connected
}

/**
 * Update status bar based on language server state
 */
function updateStatusBar(connected: boolean, configuration: AsciidocConfiguration): void {
    if (statusBarItem && configuration.ui.statusBar.enabled) {
        if (connected) {
            statusBarItem.text = constants.STATUS_BAR.TEXT;
            statusBarItem.color = undefined;
        } else {
            statusBarItem.text = '$(x) AsciiDoc';
            statusBarItem.color = 'yellow';
        }
        statusBarItem.show();
    } else if (statusBarItem && !configuration.ui.statusBar.enabled) {
        statusBarItem.hide();
    }
}