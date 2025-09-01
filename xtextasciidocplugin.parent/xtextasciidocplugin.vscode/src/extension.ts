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

    // Register Phase 3 commands
    context.subscriptions.push(
        vscode.commands.registerCommand('asciidoc.generateBoilerplate', generateBoilerplate),
        vscode.commands.registerCommand('asciidoc.generateTests', generateTests),
        vscode.commands.registerCommand('asciidoc.formatDocument', formatDocument),
        vscode.commands.registerCommand('asciidoc.validateAllFiles', validateAllFiles)
    );

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

// Phase 3 Command Implementations

async function generateBoilerplate() {
    const fileName = await vscode.window.showInputBox({
        prompt: 'Enter filename for new AsciiDoc file',
        value: 'new-document.asciidoc'
    });

    if (!fileName) return;

    const config = vscode.workspace.getConfiguration('asciidoc');
    const template = config.get<string>('boilerplate.template') || 'Hello World!\nHello AsciiDoc!\nHello VSCode!';
    
    const workspaceFolder = vscode.workspace.workspaceFolders?.[0];
    if (!workspaceFolder) {
        vscode.window.showErrorMessage('No workspace folder open');
        return;
    }

    const filePath = vscode.Uri.joinPath(workspaceFolder.uri, fileName.endsWith('.asciidoc') ? fileName : fileName + '.asciidoc');
    
    try {
        await vscode.workspace.fs.writeFile(filePath, Buffer.from(template, 'utf8'));
        const document = await vscode.workspace.openTextDocument(filePath);
        await vscode.window.showTextDocument(document);
        statusBarItem.text = '$(check) AsciiDoc - File Created';
        setTimeout(() => statusBarItem.text = '$(check) AsciiDoc', 3000);
    } catch (error) {
        vscode.window.showErrorMessage(`Failed to create file: ${error}`);
    }
}

async function generateTests() {
    const editor = vscode.window.activeTextEditor;
    if (!editor || editor.document.languageId !== 'asciidoc') {
        vscode.window.showErrorMessage('Please open an AsciiDoc file first');
        return;
    }

    const content = editor.document.getText();
    const helloStatements = content.match(/Hello\s+(\w+)!/g) || [];
    
    if (helloStatements.length === 0) {
        vscode.window.showWarningMessage('No Hello statements found to generate tests for');
        return;
    }

    const testContent = generateTestContent(helloStatements);
    const testFileName = editor.document.fileName.replace(/\.asciidoc$/, '.test.asciidoc');
    const testUri = vscode.Uri.file(testFileName);

    try {
        await vscode.workspace.fs.writeFile(testUri, Buffer.from(testContent, 'utf8'));
        const testDocument = await vscode.workspace.openTextDocument(testUri);
        await vscode.window.showTextDocument(testDocument, vscode.ViewColumn.Beside);
        vscode.window.showInformationMessage(`Generated ${helloStatements.length} test cases`);
    } catch (error) {
        vscode.window.showErrorMessage(`Failed to generate tests: ${error}`);
    }
}

function generateTestContent(helloStatements: string[]): string {
    const testFunctions = helloStatements.map((statement, index) => {
        const name = statement.match(/Hello\s+(\w+)!/)?.[1] || 'Unknown';
        return `// Test case ${index + 1}: ${statement}
Hello Test${name}!`;
    });

    return `// Generated test file
// Contains test cases for Hello statements

${testFunctions.join('\n\n')}

// End of generated tests`;
}

async function formatDocument() {
    const editor = vscode.window.activeTextEditor;
    if (!editor || editor.document.languageId !== 'asciidoc') {
        vscode.window.showErrorMessage('Please open an AsciiDoc file first');
        return;
    }

    const config = vscode.workspace.getConfiguration('asciidoc');
    if (!config.get('formatting.enabled')) {
        vscode.window.showInformationMessage('AsciiDoc formatting is disabled in settings');
        return;
    }

    const document = editor.document;
    const content = document.getText();
    
    // Apply formatting rules
    const formattedContent = content
        .split('\n')
        .map(line => {
            // Trim whitespace
            line = line.trim();
            
            // Fix comment formatting
            if (line.startsWith('//') && !line.startsWith('// ')) {
                line = '// ' + line.substring(2);
            }
            
            // Normalize Hello statements
            line = line.replace(/Hello\s+(\w+)\s*!/, 'Hello $1!');
            
            return line;
        })
        .join('\n');

    const edit = new vscode.WorkspaceEdit();
    edit.replace(document.uri, new vscode.Range(0, 0, document.lineCount, 0), formattedContent);
    
    await vscode.workspace.applyEdit(edit);
    statusBarItem.text = '$(check) AsciiDoc - Formatted';
    setTimeout(() => statusBarItem.text = '$(check) AsciiDoc', 3000);
}

async function validateAllFiles() {
    const workspaceFolder = vscode.workspace.workspaceFolders?.[0];
    if (!workspaceFolder) {
        vscode.window.showErrorMessage('No workspace folder open');
        return;
    }

    const files = await vscode.workspace.findFiles('**/*.asciidoc');
    const outputChannel = vscode.window.createOutputChannel('AsciiDoc Validation');
    outputChannel.clear();
    outputChannel.show();

    let totalFiles = files.length;
    let validFiles = 0;
    let totalIssues = 0;

    outputChannel.appendLine(`=== AsciiDoc Validation Results ===`);
    outputChannel.appendLine(`Found ${totalFiles} AsciiDoc files to validate`);
    outputChannel.appendLine('');

    for (const file of files) {
        const document = await vscode.workspace.openTextDocument(file);
        const content = document.getText();
        const issues = validateAsciiDocContent(content);
        
        if (issues.length === 0) {
            validFiles++;
            outputChannel.appendLine(`✓ ${file.fsPath} - Valid`);
        } else {
            totalIssues += issues.length;
            outputChannel.appendLine(`✗ ${file.fsPath} - ${issues.length} issue(s):`);
            issues.forEach(issue => {
                outputChannel.appendLine(`  Line ${issue.line}: ${issue.message}`);
            });
        }
    }

    outputChannel.appendLine('');
    outputChannel.appendLine(`=== Summary ===`);
    outputChannel.appendLine(`Total files: ${totalFiles}`);
    outputChannel.appendLine(`Valid files: ${validFiles}`);
    outputChannel.appendLine(`Files with issues: ${totalFiles - validFiles}`);
    outputChannel.appendLine(`Total issues: ${totalIssues}`);

    statusBarItem.text = `$(check) AsciiDoc - Validated ${totalFiles}`;
    setTimeout(() => statusBarItem.text = '$(check) AsciiDoc', 5000);
}

function validateAsciiDocContent(content: string): Array<{line: number, message: string}> {
    const issues: Array<{line: number, message: string}> = [];
    const lines = content.split('\n');

    lines.forEach((line, index) => {
        const lineNumber = index + 1;
        
        // Check for malformed Hello statements
        if (line.includes('Hello') && !line.match(/^Hello\s+\w+!$/)) {
            if (!line.startsWith('//')) {
                issues.push({
                    line: lineNumber,
                    message: 'Malformed Hello statement - should be "Hello Name!"'
                });
            }
        }
        
        // Check for comment formatting
        if (line.startsWith('//') && !line.startsWith('// ') && line.length > 2) {
            issues.push({
                line: lineNumber,
                message: 'Comment should have space after "//"'
            });
        }
        
        // Check for unrecognized statements
        if (line.trim() && !line.match(/^(Hello\s+\w+!|\/\/.*)$/)) {
            issues.push({
                line: lineNumber,
                message: 'Unrecognized statement - only Hello statements and comments are supported'
            });
        }
    });

    return issues;
}

export function deactivate(): Thenable<void> | undefined {
    if (!client) {
        return undefined;
    }
    return client.stop();
}