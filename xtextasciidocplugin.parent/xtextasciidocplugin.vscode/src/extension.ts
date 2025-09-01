import * as vscode from 'vscode';
import * as path from 'path';
import * as fs from 'fs';
import { LanguageClient, LanguageClientOptions, ServerOptions } from 'vscode-languageclient/node';

let client: LanguageClient;
let outputChannel: vscode.OutputChannel;
let statusBarItem: vscode.StatusBarItem;

export function activate(context: vscode.ExtensionContext) {
    console.log('AsciiDoc extension is being activated');
    
    // Initialize output channel for logging
    outputChannel = vscode.window.createOutputChannel('AsciiDoc Extension');
    outputChannel.appendLine('AsciiDoc Extension: Initializing...');
    context.subscriptions.push(outputChannel);

    // Configuration
    const config = vscode.workspace.getConfiguration('asciidoc');
    const enabled = config.get('languageServer.enabled', true);
    
    if (!enabled) {
        console.log('AsciiDoc language server is disabled');
        return;
    }

    // Language Server executable
    const serverJarPath = path.join(context.extensionPath, 'server', 'xtextasciidocplugin.ide-1.0.0-SNAPSHOT-standalone.jar');
    
    // Check if Java is available
    const javaExecutable = getJavaExecutable();
    if (!javaExecutable) {
        vscode.window.showErrorMessage('Java runtime is required for AsciiDoc language server. Please install Java 11 or higher.');
        return;
    }

    // Determine the correct working directory for the language server
    const workspaceFolder = vscode.workspace.workspaceFolders?.[0];
    const projectDir = workspaceFolder ? 
        path.join(workspaceFolder.uri.fsPath, 'sheep-dog-cloud', 'xtextasciidocplugin.parent') : 
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
        // Register the server for AsciiDoc documents
        documentSelector: [
            { scheme: 'file', language: 'asciidoc' }
        ],
        synchronize: {
            // Notify the server about file changes to '.asciidoc' files contained in the workspace
            fileEvents: vscode.workspace.createFileSystemWatcher('**/*.asciidoc')
        },
        outputChannel: vscode.window.createOutputChannel('AsciiDoc Language Server'),
        traceOutputChannel: vscode.window.createOutputChannel('AsciiDoc Language Server Trace')
    };

    // Create the language client
    client = new LanguageClient(
        'asciidocLanguageServer',
        'AsciiDoc Language Server',
        serverOptions,
        clientOptions
    );

    // Start the client. This will also launch the server
    console.log('Starting AsciiDoc language server...');
    client.start().then(() => {
        console.log('AsciiDoc language server started successfully');
        
        // Show status in status bar
        statusBarItem = vscode.window.createStatusBarItem(vscode.StatusBarAlignment.Right, 100);
        statusBarItem.text = '$(check) AsciiDoc';
        statusBarItem.tooltip = 'AsciiDoc Language Server is running';
        statusBarItem.show();
        context.subscriptions.push(statusBarItem);
        
        outputChannel.appendLine('AsciiDoc Extension: Language server started successfully');
        
    }).catch(error => {
        console.error('Failed to start AsciiDoc language server:', error);
        outputChannel.appendLine(`AsciiDoc Extension: Failed to start language server: ${error.message}`);
        vscode.window.showErrorMessage(`Failed to start AsciiDoc language server: ${error.message}`);
    });

    context.subscriptions.push(client);

    // Register commands
    registerCommands(context);

    outputChannel.appendLine('AsciiDoc Extension: All commands registered');
    console.log('AsciiDoc extension activated');
}

export function deactivate(): Thenable<void> | undefined {
    if (!client) {
        return undefined;
    }
    console.log('Deactivating AsciiDoc extension...');
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
    const helloCommand = vscode.commands.registerCommand('asciidoc.helloWorld', () => {
        vscode.window.showInformationMessage('Hello from AsciiDoc Extension!');
        outputChannel.appendLine('AsciiDoc Extension: Hello World command executed');
    });
    context.subscriptions.push(helloCommand);

    // Generate Boilerplate command
    const generateBoilerplateCommand = vscode.commands.registerCommand('asciidoc.generateBoilerplate', async () => {
        outputChannel.appendLine('AsciiDoc Extension: Generate Boilerplate command executed');
        try {
            await generateBoilerplateFile();
        } catch (error) {
            outputChannel.appendLine(`AsciiDoc Extension: Error in generateBoilerplate: ${error}`);
            vscode.window.showErrorMessage(`Failed to generate boilerplate: ${error}`);
        }
    });
    context.subscriptions.push(generateBoilerplateCommand);

    // Generate Tests command
    const generateTestsCommand = vscode.commands.registerCommand('asciidoc.generateTests', async () => {
        outputChannel.appendLine('AsciiDoc Extension: Generate Tests command executed');
        try {
            await generateTestsFromAsciiDoc();
        } catch (error) {
            outputChannel.appendLine(`AsciiDoc Extension: Error in generateTests: ${error}`);
            vscode.window.showErrorMessage(`Failed to generate tests: ${error}`);
        }
    });
    context.subscriptions.push(generateTestsCommand);

    // Format Document command
    const formatDocumentCommand = vscode.commands.registerCommand('asciidoc.formatDocument', async () => {
        outputChannel.appendLine('AsciiDoc Extension: Format Document command executed');
        try {
            await formatAsciiDocDocument();
        } catch (error) {
            outputChannel.appendLine(`AsciiDoc Extension: Error in formatDocument: ${error}`);
            vscode.window.showErrorMessage(`Failed to format document: ${error}`);
        }
    });
    context.subscriptions.push(formatDocumentCommand);

    // Validate All Files command
    const validateAllFilesCommand = vscode.commands.registerCommand('asciidoc.validateAllFiles', async () => {
        outputChannel.appendLine('AsciiDoc Extension: Validate All Files command executed');
        try {
            await validateAllAsciiDocFiles();
        } catch (error) {
            outputChannel.appendLine(`AsciiDoc Extension: Error in validateAllFiles: ${error}`);
            vscode.window.showErrorMessage(`Failed to validate files: ${error}`);
        }
    });
    context.subscriptions.push(validateAllFilesCommand);
}

async function generateBoilerplateFile() {
    outputChannel.appendLine('AsciiDoc Extension: Starting boilerplate generation');
    
    const config = vscode.workspace.getConfiguration('asciidoc');
    const template = config.get('boilerplate.template', '// AsciiDoc Boilerplate File\nHello World!\nHello Claude!\nHello VSCode!');
    
    // Ask user for file name
    const fileName = await vscode.window.showInputBox({
        prompt: 'Enter the name for the new AsciiDoc file',
        placeHolder: 'example.asciidoc',
        value: 'new-file.asciidoc'
    });
    
    if (!fileName) {
        outputChannel.appendLine('AsciiDoc Extension: Boilerplate generation cancelled by user');
        return;
    }
    
    // Ensure .asciidoc extension
    const normalizedFileName = fileName.endsWith('.asciidoc') ? fileName : `${fileName}.asciidoc`;
    
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
            outputChannel.appendLine('AsciiDoc Extension: Boilerplate generation cancelled - file exists');
            return;
        }
    }
    
    // Write the file
    fs.writeFileSync(filePath, template.replace(/\\n/g, '\n'), 'utf8');
    
    // Open the file
    const document = await vscode.workspace.openTextDocument(filePath);
    await vscode.window.showTextDocument(document);
    
    outputChannel.appendLine(`AsciiDoc Extension: Boilerplate file created: ${filePath}`);
    vscode.window.showInformationMessage(`AsciiDoc boilerplate file created: ${normalizedFileName}`);
    
    // Update status bar temporarily
    if (statusBarItem) {
        const originalText = statusBarItem.text;
        statusBarItem.text = '$(check) AsciiDoc - Boilerplate Created';
        setTimeout(() => {
            statusBarItem.text = originalText;
        }, 3000);
    }
}

async function generateTestsFromAsciiDoc() {
    outputChannel.appendLine('AsciiDoc Extension: Starting test generation');
    
    const activeEditor = vscode.window.activeTextEditor;
    if (!activeEditor) {
        throw new Error('No active editor found');
    }
    
    if (activeEditor.document.languageId !== 'asciidoc') {
        throw new Error('Active document is not a AsciiDoc file');
    }
    
    const document = activeEditor.document;
    const content = document.getText();
    
    outputChannel.appendLine(`AsciiDoc Extension: Analyzing content: ${content.substring(0, 100)}...`);
    
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
        outputChannel.appendLine('AsciiDoc Extension: No Hello statements found');
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
    const testPath = originalPath.replace('.asciidoc', '.test.asciidoc');
    
    // Write test file
    fs.writeFileSync(testPath, testContent, 'utf8');
    
    // Open test file
    const testDocument = await vscode.workspace.openTextDocument(testPath);
    await vscode.window.showTextDocument(testDocument, vscode.ViewColumn.Beside);
    
    outputChannel.appendLine(`AsciiDoc Extension: Test file generated: ${testPath}`);
    outputChannel.appendLine(`AsciiDoc Extension: Generated ${helloStatements.length} test cases`);
    vscode.window.showInformationMessage(`Generated tests for ${helloStatements.length} Hello statements`);
    
    // Update status bar temporarily
    if (statusBarItem) {
        const originalText = statusBarItem.text;
        statusBarItem.text = '$(check) AsciiDoc - Tests Generated';
        setTimeout(() => {
            statusBarItem.text = originalText;
        }, 3000);
    }
}

async function formatAsciiDocDocument() {
    outputChannel.appendLine('AsciiDoc Extension: Starting document formatting');
    
    const activeEditor = vscode.window.activeTextEditor;
    if (!activeEditor) {
        throw new Error('No active editor found');
    }
    
    if (activeEditor.document.languageId !== 'asciidoc') {
        throw new Error('Active document is not a AsciiDoc file');
    }
    
    const config = vscode.workspace.getConfiguration('asciidoc');
    const formattingEnabled = config.get('formatting.enabled', true);
    
    if (!formattingEnabled) {
        vscode.window.showInformationMessage('AsciiDoc formatting is disabled in settings');
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
    
    outputChannel.appendLine('AsciiDoc Extension: Document formatting completed');
    vscode.window.showInformationMessage('AsciiDoc document formatted');
    
    // Update status bar temporarily
    if (statusBarItem) {
        const originalText = statusBarItem.text;
        statusBarItem.text = '$(check) AsciiDoc - Formatted';
        setTimeout(() => {
            statusBarItem.text = originalText;
        }, 2000);
    }
}

async function validateAllAsciiDocFiles() {
    outputChannel.appendLine('AsciiDoc Extension: Starting validation of all AsciiDoc files');
    
    const config = vscode.workspace.getConfiguration('asciidoc');
    const validationEnabled = config.get('validation.enabled', true);
    
    if (!validationEnabled) {
        vscode.window.showInformationMessage('AsciiDoc validation is disabled in settings');
        return;
    }
    
    // Find all .asciidoc files in workspace
    const asciidocFiles = await vscode.workspace.findFiles('**/*.asciidoc', '**/node_modules/**');
    
    if (asciidocFiles.length === 0) {
        vscode.window.showInformationMessage('No AsciiDoc files found in workspace');
        outputChannel.appendLine('AsciiDoc Extension: No AsciiDoc files found');
        return;
    }
    
    outputChannel.appendLine(`AsciiDoc Extension: Found ${asciidocFiles.length} AsciiDoc files to validate`);
    outputChannel.show(true); // Show the output channel
    
    let validFiles = 0;
    let invalidFiles = 0;
    const validationResults: { file: string; valid: boolean; issues: string[] }[] = [];
    
    for (const fileUri of asciidocFiles) {
        const filePath = fileUri.fsPath;
        const relativePath = vscode.workspace.asRelativePath(fileUri);
        
        outputChannel.appendLine(`AsciiDoc Extension: Validating ${relativePath}...`);
        
        try {
            const content = fs.readFileSync(filePath, 'utf8');
            const issues = validateAsciiDocContent(content);
            
            if (issues.length === 0) {
                validFiles++;
                outputChannel.appendLine(`  ✓ ${relativePath} - Valid`);
                validationResults.push({ file: relativePath, valid: true, issues: [] });
            } else {
                invalidFiles++;
                outputChannel.appendLine(`  ✗ ${relativePath} - ${issues.length} issues found:`);
                for (const issue of issues) {
                    outputChannel.appendLine(`    - ${issue}`);
                }
                validationResults.push({ file: relativePath, valid: false, issues });
            }
        } catch (error) {
            invalidFiles++;
            const errorMessage = `Error reading file: ${error}`;
            outputChannel.appendLine(`  ✗ ${relativePath} - ${errorMessage}`);
            validationResults.push({ file: relativePath, valid: false, issues: [errorMessage] });
        }
    }
    
    // Summary
    outputChannel.appendLine('');
    outputChannel.appendLine('=== AsciiDoc Validation Summary ===');
    outputChannel.appendLine(`Total files: ${asciidocFiles.length}`);
    outputChannel.appendLine(`Valid files: ${validFiles}`);
    outputChannel.appendLine(`Invalid files: ${invalidFiles}`);
    
    if (invalidFiles > 0) {
        outputChannel.appendLine('');
        outputChannel.appendLine('Files with issues:');
        for (const result of validationResults) {
            if (!result.valid) {
                outputChannel.appendLine(`  - ${result.file}: ${result.issues.length} issues`);
            }
        }
    }
    
    // Show summary message
    const message = `Validation complete: ${validFiles} valid, ${invalidFiles} invalid files`;
    if (invalidFiles > 0) {
        vscode.window.showWarningMessage(message);
    } else {
        vscode.window.showInformationMessage(message);
    }
    
    // Update status bar temporarily
    if (statusBarItem) {
        const originalText = statusBarItem.text;
        statusBarItem.text = invalidFiles > 0 ? '$(warning) AsciiDoc - Issues Found' : '$(check) AsciiDoc - All Valid';
        setTimeout(() => {
            statusBarItem.text = originalText;
        }, 5000);
    }
}

function validateAsciiDocContent(content: string): string[] {
    const issues: string[] = [];
    const lines = content.split('\n');
    
    for (let i = 0; i < lines.length; i++) {
        const line = lines[i].trim();
        const lineNumber = i + 1;
        
        if (line.length === 0) continue;
        
        // Check for comments
        if (line.startsWith('//')) {
            if (line === '//') {
                issues.push(`Line ${lineNumber}: Empty comment`);
            }
            continue;
        }
        
        // Check Hello statements
        if (line.startsWith('Hello ')) {
            if (!line.endsWith('!')) {
                issues.push(`Line ${lineNumber}: Hello statement should end with '!'`);
            }
            const name = line.substring(6, line.endsWith('!') ? line.length - 1 : line.length);
            if (name.trim().length === 0) {
                issues.push(`Line ${lineNumber}: Hello statement has empty name`);
            }
            continue;
        }
        
        // Check for unrecognized patterns
        if (!line.match(/^(Hello\s+.+|\/\/.*)$/)) {
            issues.push(`Line ${lineNumber}: Unrecognized statement: "${line}"`);
        }
    }
    
    return issues;
}
