"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || (function () {
    var ownKeys = function(o) {
        ownKeys = Object.getOwnPropertyNames || function (o) {
            var ar = [];
            for (var k in o) if (Object.prototype.hasOwnProperty.call(o, k)) ar[ar.length] = k;
            return ar;
        };
        return ownKeys(o);
    };
    return function (mod) {
        if (mod && mod.__esModule) return mod;
        var result = {};
        if (mod != null) for (var k = ownKeys(mod), i = 0; i < k.length; i++) if (k[i] !== "default") __createBinding(result, mod, k[i]);
        __setModuleDefault(result, mod);
        return result;
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
exports.activate = activate;
exports.deactivate = deactivate;
const vscode = __importStar(require("vscode"));
const path = __importStar(require("path"));
const fs = __importStar(require("fs"));
const node_1 = require("vscode-languageclient/node");
let client;
let outputChannel;
let statusBarItem;
function activate(context) {
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
        path.join(workspaceFolder.uri.fsPath, 'sheep-dog-local', 'org.xtext.example.mydsl.parent') :
        undefined;
    // Server options - embedded JAR approach with correct working directory
    const serverOptions = {
        run: {
            command: javaExecutable,
            args: ['-jar', serverJarPath],
            options: {
                cwd: projectDir // Set working directory to project root
            }
        },
        debug: {
            command: javaExecutable,
            args: ['-jar', serverJarPath],
            options: {
                cwd: projectDir // Set working directory to project root
            }
        }
    };
    // Options to control the language client
    const clientOptions = {
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
    client = new node_1.LanguageClient('mydslLanguageServer', 'MyDsl Language Server', serverOptions, clientOptions);
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
function deactivate() {
    if (!client) {
        return undefined;
    }
    console.log('Deactivating MyDsl extension...');
    return client.stop();
}
function getJavaExecutable() {
    // Check for JAVA_HOME environment variable
    const javaHome = process.env.JAVA_HOME;
    if (javaHome) {
        return path.join(javaHome, 'bin', process.platform === 'win32' ? 'java.exe' : 'java');
    }
    // Fallback to java in PATH
    return process.platform === 'win32' ? 'java.exe' : 'java';
}
function registerCommands(context) {
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
        }
        catch (error) {
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
        }
        catch (error) {
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
        }
        catch (error) {
            outputChannel.appendLine(`MyDsl Extension: Error in formatDocument: ${error}`);
            vscode.window.showErrorMessage(`Failed to format document: ${error}`);
        }
    });
    context.subscriptions.push(formatDocumentCommand);
    // Validate All Files command
    const validateAllFilesCommand = vscode.commands.registerCommand('mydsl.validateAllFiles', async () => {
        outputChannel.appendLine('MyDsl Extension: Validate All Files command executed');
        try {
            await validateAllMyDslFiles();
        }
        catch (error) {
            outputChannel.appendLine(`MyDsl Extension: Error in validateAllFiles: ${error}`);
            vscode.window.showErrorMessage(`Failed to validate files: ${error}`);
        }
    });
    context.subscriptions.push(validateAllFilesCommand);
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
        const overwrite = await vscode.window.showWarningMessage(`File ${normalizedFileName} already exists. Overwrite?`, 'Yes', 'No');
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
    const helloStatements = [];
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
    const formattedLines = [];
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
    const fullRange = new vscode.Range(document.positionAt(0), document.positionAt(content.length));
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
async function validateAllMyDslFiles() {
    outputChannel.appendLine('MyDsl Extension: Starting validation of all MyDsl files');
    const config = vscode.workspace.getConfiguration('mydsl');
    const validationEnabled = config.get('validation.enabled', true);
    if (!validationEnabled) {
        vscode.window.showInformationMessage('MyDsl validation is disabled in settings');
        return;
    }
    // Find all .mydsl files in workspace
    const mydslFiles = await vscode.workspace.findFiles('**/*.mydsl', '**/node_modules/**');
    if (mydslFiles.length === 0) {
        vscode.window.showInformationMessage('No MyDsl files found in workspace');
        outputChannel.appendLine('MyDsl Extension: No MyDsl files found');
        return;
    }
    outputChannel.appendLine(`MyDsl Extension: Found ${mydslFiles.length} MyDsl files to validate`);
    outputChannel.show(true); // Show the output channel
    let validFiles = 0;
    let invalidFiles = 0;
    const validationResults = [];
    for (const fileUri of mydslFiles) {
        const filePath = fileUri.fsPath;
        const relativePath = vscode.workspace.asRelativePath(fileUri);
        outputChannel.appendLine(`MyDsl Extension: Validating ${relativePath}...`);
        try {
            const content = fs.readFileSync(filePath, 'utf8');
            const issues = validateMyDslContent(content);
            if (issues.length === 0) {
                validFiles++;
                outputChannel.appendLine(`  ✓ ${relativePath} - Valid`);
                validationResults.push({ file: relativePath, valid: true, issues: [] });
            }
            else {
                invalidFiles++;
                outputChannel.appendLine(`  ✗ ${relativePath} - ${issues.length} issues found:`);
                for (const issue of issues) {
                    outputChannel.appendLine(`    - ${issue}`);
                }
                validationResults.push({ file: relativePath, valid: false, issues });
            }
        }
        catch (error) {
            invalidFiles++;
            const errorMessage = `Error reading file: ${error}`;
            outputChannel.appendLine(`  ✗ ${relativePath} - ${errorMessage}`);
            validationResults.push({ file: relativePath, valid: false, issues: [errorMessage] });
        }
    }
    // Summary
    outputChannel.appendLine('');
    outputChannel.appendLine('=== MyDsl Validation Summary ===');
    outputChannel.appendLine(`Total files: ${mydslFiles.length}`);
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
    }
    else {
        vscode.window.showInformationMessage(message);
    }
    // Update status bar temporarily
    if (statusBarItem) {
        const originalText = statusBarItem.text;
        statusBarItem.text = invalidFiles > 0 ? '$(warning) MyDsl - Issues Found' : '$(check) MyDsl - All Valid';
        setTimeout(() => {
            statusBarItem.text = originalText;
        }, 5000);
    }
}
function validateMyDslContent(content) {
    const issues = [];
    const lines = content.split('\n');
    for (let i = 0; i < lines.length; i++) {
        const line = lines[i].trim();
        const lineNumber = i + 1;
        if (line.length === 0)
            continue;
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
//# sourceMappingURL=extension.js.map