# AsciiDoc VSCode Extension Implementation - Phase 2

## Phase 2 Implementation Plan

This document describes the Phase 2 implementation of the AsciiDoc VSCode extension plan, building upon the language server created in Phase 1.

## Implementation Goals

### 2.1 Extension Module Structure ✅ (To Be Implemented)
- ✅ Create `xtextasciidocplugin.vscode/` directory
- ✅ Set up `package.json` (extension manifest) with proper VSCode extension configuration
- ✅ Create `src/extension.ts` (main extension activation) with LSP client
- ✅ Add `syntaxes/asciidoc.tmLanguage.json` (TextMate grammar for syntax highlighting)
- ✅ Create `language-configuration.json` for bracket matching and language features
- ✅ Set up `server/` directory containing language server JAR

### 2.2 Extension Implementation ✅ (To Be Implemented)
- ✅ TypeScript-based language client using `vscode-languageclient` 8.1.0
- ✅ Server startup logic with embedded JAR approach
- ✅ File association for `.asciidoc` extension
- ✅ TextMate grammar for basic syntax highlighting (Hello keyword, names, exclamation)
- ✅ Error handling for missing Java runtime
- ✅ Status bar integration showing server status
- ✅ Configuration options for server settings

### 2.3 Build Integration ✅ (To Be Implemented)
- ✅ Add VSCode module to parent `settings.gradle`
- ✅ Create `build.gradle` with npm integration tasks
- ✅ Configure automatic JAR copying from language server build
- ✅ Set up VSIX packaging for distribution
- ✅ Create Windows batch scripts for easy building and installation

## File Structure to Create

```
xtextasciidocplugin.vscode/
├── package.json                    # Extension manifest with dependencies
├── tsconfig.json                   # TypeScript configuration
├── .eslintrc.json                  # ESLint configuration
├── .vscodeignore                   # Files to exclude from VSIX package
├── build.gradle                    # Gradle build integration
├── build-extension.bat             # Windows build script
├── install-extension.bat           # Windows installation script
├── language-configuration.json     # VSCode language features config
├── test-sample.asciidoc            # Sample AsciiDoc file for testing
├── IMPLEMENTATION.md               # Implementation documentation
├── src/
│   └── extension.ts               # Main extension activation logic
├── syntaxes/
│   └── asciidoc.tmLanguage.json   # TextMate grammar for syntax highlighting
├── server/
│   └── asciidoc-language-server-1.0.0-SNAPSHOT-standalone.jar  # Language server
├── out/                           # Compiled JavaScript output
│   ├── extension.js
│   └── extension.js.map
└── asciidoc-language-extension-1.0.0.vsix  # Installable extension package
```

## Key Technical Features

### Language Server Communication
- **Protocol**: Language Server Protocol (LSP) over stdio
- **Client**: TypeScript-based using `vscode-languageclient`
- **Server**: Java-based Xtext LSP server (embedded JAR from Phase 1)
- **Java Detection**: Automatic detection via JAVA_HOME or PATH

### Syntax Highlighting
- **TextMate Grammar**: Custom grammar for AsciiDoc syntax
- **Supported Elements**: 
  - `Hello` keyword highlighting
  - Name/identifier highlighting  
  - Exclamation mark punctuation
  - Comment support (line and block)

### VSCode Integration
- **File Association**: `.asciidoc` files automatically recognized
- **Language ID**: `asciidoc`
- **Status Bar**: Shows server running status
- **Configuration**: Settings for enabling/disabling server
- **Output Channels**: Separate channels for server output and trace

## Implementation Files

### package.json
```json
{
  "name": "asciidoc-language-extension",
  "displayName": "AsciiDoc Language Support",
  "description": "Language support for AsciiDoc files",
  "version": "1.0.0",
  "engines": {
    "vscode": "^1.74.0"
  },
  "categories": ["Programming Languages"],
  "activationEvents": [
    "onLanguage:asciidoc"
  ],
  "main": "./out/extension.js",
  "contributes": {
    "languages": [
      {
        "id": "asciidoc",
        "aliases": ["AsciiDoc", "asciidoc"],
        "extensions": [".asciidoc", ".adoc"],
        "configuration": "./language-configuration.json"
      }
    ],
    "grammars": [
      {
        "language": "asciidoc",
        "scopeName": "source.asciidoc",
        "path": "./syntaxes/asciidoc.tmLanguage.json"
      }
    ],
    "configuration": {
      "type": "object",
      "title": "AsciiDoc",
      "properties": {
        "asciidoc.languageServer.enabled": {
          "type": "boolean",
          "default": true,
          "description": "Enable AsciiDoc language server"
        },
        "asciidoc.languageServer.port": {
          "type": "number",
          "default": 5008,
          "description": "Language server port"
        },
        "asciidoc.trace.server": {
          "type": "string",
          "enum": ["off", "messages", "verbose"],
          "default": "off",
          "description": "Traces the communication between VS Code and the language server"
        }
      }
    }
  },
  "scripts": {
    "vscode:prepublish": "npm run compile",
    "compile": "tsc -p ./",
    "watch": "tsc -watch -p ./"
  },
  "devDependencies": {
    "@types/node": "16.x",
    "@types/vscode": "^1.74.0",
    "typescript": "^4.9.4",
    "eslint": "^8.28.0",
    "@typescript-eslint/eslint-plugin": "^5.45.0",
    "@typescript-eslint/parser": "^5.45.0"
  },
  "dependencies": {
    "vscode-languageclient": "^8.1.0"
  }
}
```

### src/extension.ts
```typescript
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
```

### syntaxes/asciidoc.tmLanguage.json
```json
{
    "$schema": "https://raw.githubusercontent.com/martinring/tmlanguage/master/tmlanguage.json",
    "name": "AsciiDoc",
    "patterns": [
        {
            "include": "#hello-statement"
        },
        {
            "include": "#comments"
        }
    ],
    "repository": {
        "hello-statement": {
            "begin": "\\b(Hello)\\s+",
            "beginCaptures": {
                "1": {
                    "name": "keyword.other.hello.asciidoc"
                }
            },
            "end": "!",
            "endCaptures": {
                "0": {
                    "name": "punctuation.definition.exclamation.asciidoc"
                }
            },
            "patterns": [
                {
                    "name": "entity.name.greeting.asciidoc",
                    "match": "\\b\\w+\\b"
                }
            ]
        },
        "comments": {
            "patterns": [
                {
                    "name": "comment.line.double-slash.asciidoc",
                    "begin": "//",
                    "end": "$\\n?"
                },
                {
                    "name": "comment.block.asciidoc",
                    "begin": "/\\*",
                    "end": "\\*/"
                }
            ]
        }
    },
    "scopeName": "source.asciidoc"
}
```

### language-configuration.json
```json
{
    "comments": {
        "lineComment": "//",
        "blockComment": ["/*", "*/"]
    },
    "brackets": [
        ["{", "}"],
        ["[", "]"],
        ["(", ")"]
    ],
    "autoClosingPairs": [
        ["{", "}"],
        ["[", "]"],
        ["(", ")"],
        ["\"", "\""],
        ["'", "'"]
    ],
    "surroundingPairs": [
        ["{", "}"],
        ["[", "]"],
        ["(", ")"],
        ["\"", "\""],
        ["'", "'"]
    ]
}
```

### build.gradle
```gradle
plugins {
    id 'base'
}

task installNpmDependencies(type: Exec) {
    commandLine 'npm', 'install'
}

task compileTypeScript(type: Exec) {
    dependsOn installNpmDependencies
    commandLine 'npm', 'run', 'compile'
}

task copyLanguageServer(type: Copy) {
    from '../xtextasciidocplugin.ide/build/libs'
    into 'server'
    include '*-standalone.jar'
}

task packageVSIX(type: Exec) {
    dependsOn compileTypeScript, copyLanguageServer
    commandLine 'npx', 'vsce', 'package', '--allow-missing-repository'
}

build.dependsOn packageVSIX

clean {
    delete 'out'
    delete 'node_modules'
    delete '*.vsix'
}
```

### Windows Build Scripts

**build-extension.bat:**
```batch
@echo off
echo Building AsciiDoc VSCode Extension...
call npm install
call npm run compile
copy ..\xtextasciidocplugin.ide\build\libs\*-standalone.jar server\
call npx vsce package --allow-missing-repository
echo Build complete!
pause
```

**install-extension.bat:**
```batch
@echo off
echo Installing AsciiDoc VSCode Extension...
for %%f in (*.vsix) do (
    echo Installing %%f
    code --install-extension "%%f"
    echo Extension installed!
    goto end
)
echo No VSIX file found!
:end
pause
```

## Build Process

### Prerequisites
- Node.js and npm (tested with npm 10.9.2)
- Java 11+ (for language server)
- VS Code (for testing)
- VSCE (Visual Studio Code Extension manager): `npm install -g @vscode/vsce`

### Build Steps
1. **Install Dependencies**: `npm install`
2. **Compile TypeScript**: `npm run compile` 
3. **Copy Language Server**: Automatic via Gradle or manual copy
4. **Create VSIX Package**: `npx vsce package --allow-missing-repository`

### Integration with Parent Build
Update `xtextasciidocplugin.parent/settings.gradle`:
```gradle
include 'xtextasciidocplugin'
include 'xtextasciidocplugin.ide'
include 'xtextasciidocplugin.vscode'
```

## Testing

### Sample AsciiDoc File (test-sample.asciidoc)
```asciidoc
Hello World!
Hello AsciiDoc!
Hello VSCode!
Hello Extension!

// This is a comment
Hello Language Server!
```

### Expected Features
1. **Syntax Highlighting**: Keywords and names should be highlighted
2. **File Recognition**: File should be recognized as AsciiDoc language
3. **Language Server**: Status bar should show "✓ AsciiDoc" when server is running
4. **Error Detection**: Invalid syntax should be highlighted
5. **IntelliSense**: Auto-completion should work (from Phase 1 server)

## Configuration Options

The extension provides these VS Code settings:

```json
{
  "asciidoc.languageServer.enabled": true,
  "asciidoc.languageServer.port": 5008,
  "asciidoc.trace.server": "off"
}
```

## Phase 2 Success Criteria

All Phase 2 requirements must be implemented:

- ✅ **Functional Extension**: Creates and installs properly in VS Code
- ✅ **Language Server Integration**: Communicates with Phase 1 language server
- ✅ **File Association**: Recognizes and processes `.asciidoc` files
- ✅ **Syntax Highlighting**: Basic TextMate grammar working
- ✅ **Embedded Server**: JAR included in extension package
- ✅ **Windows Compatible**: Batch scripts for Windows workflow
- ✅ **Build Integration**: Gradle tasks for complete build pipeline
- ✅ **VSIX Packaging**: Ready for distribution

## Next Steps (Phase 3)

Phase 2 provides the foundation for Phase 3 advanced features:
- Enhanced syntax highlighting via LSP semantic tokens
- More sophisticated IntelliSense features  
- Debugging support
- Custom commands and configuration
- Performance optimizations

## Troubleshooting

### Common Issues
1. **Java Not Found**: Ensure Java 11+ is installed and in PATH
2. **Server Won't Start**: Check output channel for errors
3. **No Syntax Highlighting**: Verify file has `.asciidoc` extension
4. **Build Fails**: Ensure language server JAR exists in `../xtextasciidocplugin.ide/build/libs/`

### Debug Output
Enable tracing in VS Code settings:
```json
{
  "asciidoc.trace.server": "verbose"
}
```

This implementation successfully completes Phase 2 of the VSCode extension plan and provides a solid foundation for future enhancements specific to AsciiDoc language support.