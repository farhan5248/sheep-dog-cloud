import * as vscode from 'vscode';
import * as path from 'path';
import { spawn } from 'child_process';
import {
    LanguageClient,
    LanguageClientOptions,
    ServerOptions,
    TransportKind
} from 'vscode-languageclient/node';

let client: LanguageClient;
let statusBarItem: vscode.StatusBarItem;

export function activate(context: vscode.ExtensionContext) {
    console.log('AsciiDoc extension is now active!');

    // Create status bar item
    statusBarItem = vscode.window.createStatusBarItem(vscode.StatusBarAlignment.Left);
    statusBarItem.text = '$(loading~spin) AsciiDoc';
    statusBarItem.show();
    context.subscriptions.push(statusBarItem);

    // Start language server
    startLanguageServer(context);
}

function startLanguageServer(context: vscode.ExtensionContext) {
    const config = vscode.workspace.getConfiguration('asciidoc');
    
    if (!config.get('languageServer.enabled')) {
        statusBarItem.text = '$(circle-slash) AsciiDoc Disabled';
        return;
    }

    const serverJar = path.join(context.extensionPath, 'server', 'asciidoc-language-server-1.0.0-SNAPSHOT-standalone.jar');
    
    // Check if Java is available
    const javaCommand = getJavaCommand();
    if (!javaCommand) {
        vscode.window.showErrorMessage('Java runtime not found. Please install Java 11+ to use AsciiDoc language features.');
        statusBarItem.text = '$(error) AsciiDoc - No Java';
        return;
    }

    // Server options
    const serverOptions: ServerOptions = {
        run: {
            command: javaCommand,
            args: ['-jar', serverJar],
            transport: TransportKind.stdio
        },
        debug: {
            command: javaCommand,
            args: ['-jar', serverJar],
            transport: TransportKind.stdio
        }
    };

    // Client options
    const clientOptions: LanguageClientOptions = {
        documentSelector: [{ scheme: 'file', language: 'asciidoc' }],
        synchronize: {
            fileEvents: vscode.workspace.createFileSystemWatcher('**/.asciidoc')
        },
        outputChannelName: 'AsciiDoc Language Server'
    };

    // Create and start the client
    client = new LanguageClient(
        'asciidocLanguageServer',
        'AsciiDoc Language Server',
        serverOptions,
        clientOptions
    );

    client.start().then(() => {
        statusBarItem.text = '$(check) AsciiDoc';
        console.log('AsciiDoc Language Server started successfully');
    }).catch((error) => {
        statusBarItem.text = '$(error) AsciiDoc Failed';
        console.error('Failed to start AsciiDoc Language Server:', error);
        vscode.window.showErrorMessage(`Failed to start AsciiDoc Language Server: ${error.message}`);
    });

    context.subscriptions.push(client);
}

function getJavaCommand(): string | null {
    // Try JAVA_HOME first
    const javaHome = process.env.JAVA_HOME;
    if (javaHome) {
        return path.join(javaHome, 'bin', 'java');
    }
    
    // Fall back to PATH
    return 'java';
}

export function deactivate(): Thenable<void> | undefined {
    if (!client) {
        return undefined;
    }
    return client.stop();
}