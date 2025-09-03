'use strict';

import * as vscode from 'vscode';
import * as path from 'path';
import * as os from 'os';
import { Trace } from 'vscode-jsonrpc';
import { LanguageClient, LanguageClientOptions, ServerOptions, State } from 'vscode-languageclient/node';
import * as constants from './constants';
import { ConfigurationService, AsciidocConfiguration, ConfigurationChangeEvent } from './configurationService';

// Global extension state
let client: LanguageClient | undefined;
let outputChannel: vscode.OutputChannel | undefined;
let statusBarItem: vscode.StatusBarItem | undefined;
let configurationService: ConfigurationService | undefined;

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
        
        // Initialize language server
        initializeLanguageServer(context, configuration);
        
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
    
    // Stop language client
    if (!client) {
        return undefined;
    }
    
    const clientToStop = client;
    client = undefined;
    
    return clientToStop.stop().then(() => {
        outputChannel?.appendLine('Xtext AsciiDoc Extension: Deactivated successfully');
    }).catch((error) => {
        const errorMessage = error instanceof Error ? error.message : 'Unknown error';
        outputChannel?.appendLine(`Xtext AsciiDoc Extension: Error during deactivation: ${errorMessage}`);
    });
}

/**
 * Initialize the language server
 */
function initializeLanguageServer(context: vscode.ExtensionContext, configuration: AsciidocConfiguration): void {
    outputChannel?.appendLine(`Log directory: ${configuration.logDirectory}`);
    outputChannel?.appendLine(`Debug mode: ${configuration.debug.enabled}`);
    outputChannel?.appendLine(`Server port: ${configuration.languageServer.port}`);
    outputChannel?.appendLine(`Server timeout: ${configuration.languageServer.timeout}ms`);
    outputChannel?.appendLine(`Max retries: ${configuration.languageServer.maxRetries}`);

    // Determine server executable
    const launcher = os.platform() === 'win32' 
        ? constants.SERVER_EXECUTABLES.WINDOWS 
        : constants.SERVER_EXECUTABLES.UNIX;
    
    const script = context.asAbsolutePath(path.join(...constants.SERVER_PATHS.RELATIVE_PATH, launcher));
    outputChannel?.appendLine(`Resolved script path: ${script}`);

    // Configure server options
    const serverOptions: ServerOptions = createServerOptions(script, configuration);
    
    // Configure client options
    const clientOptions: LanguageClientOptions = createClientOptions(configuration);

    // Create and start the language client
    client = new LanguageClient(
        'xtextAsciiDocLanguageServer',
        constants.LANGUAGE_SERVER_NAME,
        serverOptions,
        clientOptions
    );

    // Configure tracing
    const trace = getTraceLevel(configuration.trace.server || configuration.trace.level);
    client.setTrace(trace);

    // Start the client with retry logic
    startLanguageServerWithRetry(configuration).then(() => {
        outputChannel?.appendLine('Xtext AsciiDoc language server started successfully');
        updateStatusBar(true, configuration);
    }).catch(error => {
        const errorMessage = error instanceof Error ? error.message : 'Unknown error';
        outputChannel?.appendLine(`Failed to start Xtext AsciiDoc language server: ${errorMessage}`);
        
        if (configuration.ui.notifications.errors) {
            vscode.window.showErrorMessage(`Failed to start Xtext AsciiDoc language server: ${errorMessage}`);
        }
        updateStatusBar(false, configuration);
    });

    context.subscriptions.push(client);
}

/**
 * Create server options based on platform and configuration
 */
function createServerOptions(script: string, configuration: AsciidocConfiguration): ServerOptions {
    const env = createEnvironment(configuration);
    
    if (os.platform() === 'win32') {
        return {
            run: { 
                command: script, 
                options: { shell: true, env } 
            },
            debug: { 
                command: script, 
                options: { shell: true, env } 
            }
        };
    } else {
        return {
            run: { 
                command: script, 
                options: { env } 
            },
            debug: { 
                command: script, 
                args: [], 
                options: { env } 
            }
        };
    }
}

/**
 * Create environment variables for the server process
 */
function createEnvironment(configuration: AsciidocConfiguration): NodeJS.ProcessEnv {
    const env = { ...process.env };
    
    // Set log directory
    env[constants.SERVER_PATHS.LOG_DIR_KEY] = configuration.logDirectory;
    
    // Set server port
    env.SERVER_PORT = configuration.languageServer.port.toString();
    
    // Set server timeout
    env.SERVER_TIMEOUT = configuration.languageServer.timeout.toString();
    
    // Set debug options if debug mode is enabled
    if (configuration.debug.enabled) {
        const debugPort = configuration.debug.port;
        env.JAVA_OPTS = `-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=${debugPort},suspend=n,quiet=y`;
        
        if (configuration.debug.verboseLogging) {
            env.DEBUG_VERBOSE = 'true';
        }
    }
    
    // Set performance options
    env.MAX_FILE_SIZE = configuration.performance.maxFileSize.toString();
    env.BACKGROUND_PROCESSING = configuration.performance.enableBackgroundProcessing.toString();
    
    return env;
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

/**
 * Start language server with retry logic
 */
async function startLanguageServerWithRetry(configuration: AsciidocConfiguration): Promise<void> {
    if (!client) {
        throw new Error('Language client not initialized');
    }

    const maxRetries = configuration.languageServer.maxRetries;
    let retryCount = 0;

    while (retryCount <= maxRetries) {
        try {
            outputChannel?.appendLine(`Starting language server (attempt ${retryCount + 1}/${maxRetries + 1})`);
            await client.start();
            return; // Success
        } catch (error) {
            retryCount++;
            const errorMessage = error instanceof Error ? error.message : 'Unknown error';
            outputChannel?.appendLine(`Language server start attempt ${retryCount} failed: ${errorMessage}`);
            
            if (retryCount <= maxRetries) {
                const delayMs = Math.min(1000 * Math.pow(2, retryCount - 1), 10000); // Exponential backoff, max 10s
                outputChannel?.appendLine(`Retrying in ${delayMs}ms...`);
                await new Promise(resolve => setTimeout(resolve, delayMs));
            } else {
                throw error; // Re-throw after all retries exhausted
            }
        }
    }
}

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
 * Register extension commands
 */
function registerCommands(context: vscode.ExtensionContext): void {
    // Register proxy command
    const proxyCommand = vscode.commands.registerCommand(constants.COMMANDS.ASCIIDOC_A_PROXY, async () => {
        outputChannel?.appendLine('Executing AsciiDoc proxy command');
        
        const activeEditor = vscode.window.activeTextEditor;
        if (!activeEditor || !activeEditor.document || activeEditor.document.languageId !== constants.LANGUAGE_ID) {
            vscode.window.showWarningMessage('Please open an AsciiDoc file to use this command');
            return;
        }

        if (activeEditor.document.uri instanceof vscode.Uri) {
            await vscode.commands.executeCommand(constants.COMMANDS.ASCIIDOC_A, activeEditor.document.uri.toString());
        }
    });
    
    context.subscriptions.push(proxyCommand);
    outputChannel?.appendLine('Commands registered successfully');
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
            if (currentConfig && statusBarItem) {
                updateStatusBar(client?.state === State.Running, currentConfig);
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
                    await restartLanguageServer(context, event.newConfig);
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
 * Restart the language server with new configuration
 */
async function restartLanguageServer(context: vscode.ExtensionContext, configuration: AsciidocConfiguration): Promise<void> {
    if (client) {
        outputChannel?.appendLine('Stopping current language server...');
        await client.stop();
        client.dispose();
        client = undefined;
    }
    
    outputChannel?.appendLine('Starting language server with new configuration...');
    initializeLanguageServer(context, configuration);
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