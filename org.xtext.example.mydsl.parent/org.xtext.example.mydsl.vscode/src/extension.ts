import * as vscode from 'vscode';
import * as path from 'path';
import * as fs from 'fs';
import { LanguageClient, LanguageClientOptions, ServerOptions } from 'vscode-languageclient/node';

let client: LanguageClient;
let outputChannel: vscode.OutputChannel;
let statusBarItem: vscode.StatusBarItem;

export function activate(context: vscode.ExtensionContext) {
    console.log('MyDsl extension is being activated');
    
    // Initialize output channel for logging
    outputChannel = vscode.window.createOutputChannel('MyDsl Extension');
    outputChannel.appendLine('MyDsl Extension: Initializing...');
    context.subscriptions.push(outputChannel);

    // Configuration
    const config = vscode.workspace.getConfiguration('mydsl');
    const enabled = config.get('languageServer.enabled', true);
    
    if (!enabled) {
        console.log('MyDsl language server is disabled');
        return;
    }

    // Language Server executable
    const serverJarPath = path.join(context.extensionPath, 'server', 'mydsl-language-server-1.0.0-SNAPSHOT-standalone.jar');
    
    // Check if Java is available
    const javaExecutable = getJavaExecutable();
    if (!javaExecutable) {
        vscode.window.showErrorMessage('Java runtime is required for MyDsl language server. Please install Java 11 or higher.');
        return;
    }

    // Determine the correct working directory for the language server
    const workspaceFolder = vscode.workspace.workspaceFolders?.[0];
    const projectDir = workspaceFolder ? 
        path.join(workspaceFolder.uri.fsPath, 'sheep-dog-cloud', 'org.xtext.example.mydsl.parent') : 
        undefined;

    // Server options - embedded JAR approach with correct working directory
    const serverOptions: ServerOptions = {
        run: {
            command: javaExecutable,
            args: ['-jar', serverJarPath],
            options: {
                cwd: projectDir  // Set working directory to project root
            }
        },
        debug: {
            command: javaExecutable,
            args: ['-jar', serverJarPath],
            options: {
                cwd: projectDir  // Set working directory to project root
            }
        }
    };

    // Options to control the language client
    const clientOptions: LanguageClientOptions = {
        // Register the server for MyDsl documents
        documentSelector: [
            { scheme: 'file', language: 'mydsl' }
        ],
        synchronize: {
            // Notify the server about file changes to '.mydsl' files contained in the workspace
            fileEvents: vscode.workspace.createFileSystemWatcher('**/*.mydsl')
        },
        outputChannel: vscode.window.createOutputChannel('MyDsl Language Server'),
        traceOutputChannel: vscode.window.createOutputChannel('MyDsl Language Server Trace')
    };

    // Create the language client
    client = new LanguageClient(
        'mydslLanguageServer',
        'MyDsl Language Server',
        serverOptions,
        clientOptions
    );

    // Start the client. This will also launch the server
    console.log('Starting MyDsl language server...');
    client.start().then(() => {
        console.log('MyDsl language server started successfully');
        
        // Show status in status bar
        statusBarItem = vscode.window.createStatusBarItem(vscode.StatusBarAlignment.Right, 100);
        statusBarItem.text = '$(check) MyDsl';
        statusBarItem.tooltip = 'MyDsl Language Server is running';
        statusBarItem.show();
        context.subscriptions.push(statusBarItem);
        
        outputChannel.appendLine('MyDsl Extension: Language server started successfully');
        
    }).catch(error => {
        console.error('Failed to start MyDsl language server:', error);
        outputChannel.appendLine(`MyDsl Extension: Failed to start language server: ${error.message}`);
        vscode.window.showErrorMessage(`Failed to start MyDsl language server: ${error.message}`);
    });

    context.subscriptions.push(client);

    // Register commands
    registerCommands(context);

    outputChannel.appendLine('MyDsl Extension: All commands registered');
    console.log('MyDsl extension activated');
}

export function deactivate(): Thenable<void> | undefined {
    if (!client) {
        return undefined;
    }
    console.log('Deactivating MyDsl extension...');
    return client.stop();
}

function getJavaExecutable(): string | null {
    // Check for JAVA_HOME environment variable
    const javaHome = process.env.JAVA_HOME;
    if (javaHome) {
        return path.join(javaHome, 'bin', process.platform === 'win32' ? 'java.exe' : 'java');
    }

    // Fallback to java in PATH
    return process.platform === 'win32' ? 'java.exe' : 'java';
}

function registerCommands(context: vscode.ExtensionContext) {
    // Hello World command (existing)
    const helloCommand = vscode.commands.registerCommand('mydsl.helloWorld', () => {
        vscode.window.showInformationMessage('Hello from MyDsl Extension!');
        outputChannel.appendLine('MyDsl Extension: Hello World command executed');
    });
    context.subscriptions.push(helloCommand);

    // Generate Boilerplate command
    const generateBoilerplateCommand = vscode.commands.registerCommand('mydsl.generateBoilerplate', async () => {
        outputChannel.appendLine('MyDsl Extension: Generate Boilerplate command executed');
        try {
            await generateBoilerplateFile();
        } catch (error) {
            outputChannel.appendLine(`MyDsl Extension: Error in generateBoilerplate: ${error}`);
            vscode.window.showErrorMessage(`Failed to generate boilerplate: ${error}`);
        }
    });
    context.subscriptions.push(generateBoilerplateCommand);

    // Generate Tests command
    const generateTestsCommand = vscode.commands.registerCommand('mydsl.generateTests', async () => {
        outputChannel.appendLine('MyDsl Extension: Generate Tests command executed');
        try {
            await generateTestsFromMyDsl();
        } catch (error) {
            outputChannel.appendLine(`MyDsl Extension: Error in generateTests: ${error}`);
            vscode.window.showErrorMessage(`Failed to generate tests: ${error}`);
        }
    });
    context.subscriptions.push(generateTestsCommand);

    // Format Document command
    const formatDocumentCommand = vscode.commands.registerCommand('mydsl.formatDocument', async () => {
        outputChannel.appendLine('MyDsl Extension: Format Document command executed');
        try {
            await formatMyDslDocument();
        } catch (error) {
            outputChannel.appendLine(`MyDsl Extension: Error in formatDocument: ${error}`);
            vscode.window.showErrorMessage(`Failed to format document: ${error}`);
        }
    });
    context.subscriptions.push(formatDocumentCommand);
}

async function generateBoilerplateFile() {
    outputChannel.appendLine('MyDsl Extension: Starting boilerplate generation');
    
    const config = vscode.workspace.getConfiguration('mydsl');
    const template = config.get('boilerplate.template', '// MyDsl Boilerplate File\nHello World!\nHello Claude!\nHello VSCode!');
    
    // Ask user for file name
    const fileName = await vscode.window.showInputBox({
        prompt: 'Enter the name for the new MyDsl file',
        placeHolder: 'example.mydsl',
        value: 'new-file.mydsl'
    });
    
    if (!fileName) {
        outputChannel.appendLine('MyDsl Extension: Boilerplate generation cancelled by user');
        return;
    }
    
    // Ensure .mydsl extension
    const normalizedFileName = fileName.endsWith('.mydsl') ? fileName : `${fileName}.mydsl`;
    
    // Get workspace folder
    const workspaceFolder = vscode.workspace.workspaceFolders?.[0];
    if (!workspaceFolder) {
        throw new Error('No workspace folder found');
    }
    
    const filePath = path.join(workspaceFolder.uri.fsPath, normalizedFileName);
    
    // Check if file already exists
    if (fs.existsSync(filePath)) {
        const overwrite = await vscode.window.showWarningMessage(
            `File ${normalizedFileName} already exists. Overwrite?`,
            'Yes', 'No'
        );
        if (overwrite !== 'Yes') {
            outputChannel.appendLine('MyDsl Extension: Boilerplate generation cancelled - file exists');
            return;
        }
    }
    
    // Write the file
    fs.writeFileSync(filePath, template.replace(/\\n/g, '\n'), 'utf8');
    
    // Open the file
    const document = await vscode.workspace.openTextDocument(filePath);
    await vscode.window.showTextDocument(document);
    
    outputChannel.appendLine(`MyDsl Extension: Boilerplate file created: ${filePath}`);
    vscode.window.showInformationMessage(`MyDsl boilerplate file created: ${normalizedFileName}`);
    
    // Update status bar temporarily
    if (statusBarItem) {
        const originalText = statusBarItem.text;
        statusBarItem.text = '$(check) MyDsl - Boilerplate Created';
        setTimeout(() => {
            statusBarItem.text = originalText;
        }, 3000);
    }
}

async function generateTestsFromMyDsl() {
    outputChannel.appendLine('MyDsl Extension: Starting test generation');
    
    const activeEditor = vscode.window.activeTextEditor;
    if (!activeEditor) {
        throw new Error('No active editor found');
    }
    
    if (activeEditor.document.languageId !== 'mydsl') {
        throw new Error('Active document is not a MyDsl file');
    }
    
    const document = activeEditor.document;
    const content = document.getText();
    
    outputChannel.appendLine(`MyDsl Extension: Analyzing content: ${content.substring(0, 100)}...`);
    
    // Parse Hello statements
    const helloStatements: string[] = [];
    const lines = content.split('\n');
    
    for (const line of lines) {
        const trimmed = line.trim();
        if (trimmed.startsWith('Hello ') && trimmed.endsWith('!')) {
            const name = trimmed.substring(6, trimmed.length - 1);
            helloStatements.push(name);
        }
    }
    
    if (helloStatements.length === 0) {
        vscode.window.showInformationMessage('No "Hello [name]!" statements found to generate tests for.');
        outputChannel.appendLine('MyDsl Extension: No Hello statements found');
        return;
    }
    
    // Generate test content
    let testContent = '// Generated test file for ' + path.basename(document.fileName) + '\n';
    testContent += '// Generated on: ' + new Date().toISOString() + '\n\n';
    
    for (const name of helloStatements) {
        testContent += `// Test case for Hello ${name}!\n`;
        testContent += `test_hello_${name.toLowerCase().replace(/[^a-z0-9]/g, '_')} {\n`;
        testContent += `    // Verify Hello ${name}! statement\n`;
        testContent += `    assert_greeting("${name}");\n`;
        testContent += `}\n\n`;
    }
    
    // Create test file name
    const originalPath = document.fileName;
    const testPath = originalPath.replace('.mydsl', '.test.mydsl');
    
    // Write test file
    fs.writeFileSync(testPath, testContent, 'utf8');
    
    // Open test file
    const testDocument = await vscode.workspace.openTextDocument(testPath);
    await vscode.window.showTextDocument(testDocument, vscode.ViewColumn.Beside);
    
    outputChannel.appendLine(`MyDsl Extension: Test file generated: ${testPath}`);
    outputChannel.appendLine(`MyDsl Extension: Generated ${helloStatements.length} test cases`);
    vscode.window.showInformationMessage(`Generated tests for ${helloStatements.length} Hello statements`);
    
    // Update status bar temporarily
    if (statusBarItem) {
        const originalText = statusBarItem.text;
        statusBarItem.text = '$(check) MyDsl - Tests Generated';
        setTimeout(() => {
            statusBarItem.text = originalText;
        }, 3000);
    }
}

async function formatMyDslDocument() {
    outputChannel.appendLine('MyDsl Extension: Starting document formatting');
    
    const activeEditor = vscode.window.activeTextEditor;
    if (!activeEditor) {
        throw new Error('No active editor found');
    }
    
    if (activeEditor.document.languageId !== 'mydsl') {
        throw new Error('Active document is not a MyDsl file');
    }
    
    const config = vscode.workspace.getConfiguration('mydsl');
    const formattingEnabled = config.get('formatting.enabled', true);
    
    if (!formattingEnabled) {
        vscode.window.showInformationMessage('MyDsl formatting is disabled in settings');
        return;
    }
    
    const document = activeEditor.document;
    const content = document.getText();
    
    // Custom formatting logic
    const lines = content.split('\n');
    const formattedLines: string[] = [];
    
    for (let line of lines) {
        // Trim whitespace
        line = line.trim();
        
        if (line.length === 0) {
            formattedLines.push('');
            continue;
        }
        
        // Format comments
        if (line.startsWith('//')) {
            if (!line.startsWith('// ')) {
                line = line.replace('//', '// ');
            }
            formattedLines.push(line);
            continue;
        }
        
        // Format Hello statements
        if (line.match(/^Hello\s+.+!$/)) {
            // Ensure proper spacing
            line = line.replace(/Hello\s+/, 'Hello ');
            formattedLines.push(line);
            continue;
        }
        
        // Default: just add the line
        formattedLines.push(line);
    }
    
    const formattedContent = formattedLines.join('\n');
    
    // Apply formatting
    const edit = new vscode.WorkspaceEdit();
    const fullRange = new vscode.Range(
        document.positionAt(0),
        document.positionAt(content.length)
    );
    edit.replace(document.uri, fullRange, formattedContent);
    
    await vscode.workspace.applyEdit(edit);
    
    outputChannel.appendLine('MyDsl Extension: Document formatting completed');
    vscode.window.showInformationMessage('MyDsl document formatted');
    
    // Update status bar temporarily
    if (statusBarItem) {
        const originalText = statusBarItem.text;
        statusBarItem.text = '$(check) MyDsl - Formatted';
        setTimeout(() => {
            statusBarItem.text = originalText;
        }, 2000);
    }
}

