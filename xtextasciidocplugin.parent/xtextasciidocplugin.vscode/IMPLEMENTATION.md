# AsciiDoc VSCode Extension Implementation

## Overview

This VSCode extension provides language support for AsciiDoc files through a Language Server Protocol (LSP) implementation. It integrates with the Xtext-based AsciiDoc language server to provide features like syntax highlighting, error detection, and IntelliSense.

## Architecture

### Components

1. **Extension Client** (`src/extension.ts`): TypeScript-based VSCode extension that manages the language server lifecycle
2. **Language Server** (`server/`): Java-based Xtext LSP server (standalone JAR)
3. **TextMate Grammar** (`syntaxes/asciidoc.tmLanguage.json`): Provides basic syntax highlighting
4. **Language Configuration** (`language-configuration.json`): Defines bracket matching, commenting, etc.

### Communication Flow

```
VSCode Editor <---> Extension Client <---> Language Server (Xtext) <---> AsciiDoc Grammar
```

## Features

### Current Implementation

- **File Association**: Recognizes `.asciidoc` and `.adoc` files
- **Syntax Highlighting**: Basic TextMate grammar for Hello statements and comments
- **Language Server**: Full LSP integration with Xtext backend
- **Status Bar**: Shows server running status with error handling
- **Configuration**: Settings for enabling/disabling server and tracing
- **Java Detection**: Automatic detection of Java runtime

### Language Server Features (via Xtext LSP)

- Error detection and validation
- IntelliSense/code completion  
- Hover information
- Go to definition
- Symbol outline
- Document formatting

## Build Process

### Prerequisites

- Node.js and npm (10.9.2+)
- Java 11+ (for language server)
- VS Code
- VSCE: `npm install -g @vscode/vsce`

### Build Steps

1. **Install Dependencies**: `npm install`
2. **Compile TypeScript**: `npm run compile`
3. **Copy Language Server**: Automatic via Gradle
4. **Package Extension**: `npx vsce package --allow-missing-repository`

### Windows Scripts

- `build-extension.bat`: Complete build process
- `install-extension.bat`: Install VSIX in VS Code

## Configuration

The extension provides these VS Code settings:

```json
{
  "asciidoc.languageServer.enabled": true,
  "asciidoc.languageServer.port": 5008,
  "asciidoc.trace.server": "off"
}
```

## Testing

Use the provided `test-sample.asciidoc` file to verify:

1. Syntax highlighting works for Hello statements
2. Language server starts correctly (status bar shows ✓ AsciiDoc)
3. Error detection for invalid syntax
4. IntelliSense features work

## Troubleshooting

### Common Issues

1. **Java Not Found**: Install Java 11+ and ensure it's in PATH or JAVA_HOME
2. **Server Won't Start**: Check VS Code output channels for errors
3. **No Syntax Highlighting**: Verify file has `.asciidoc` extension
4. **Build Fails**: Ensure language server JAR exists in IDE project

### Debug Output

Enable verbose tracing:
```json
{
  "asciidoc.trace.server": "verbose"
}
```

Check output in VS Code: View → Output → "AsciiDoc Language Server"

## File Structure

```
xtextasciidocplugin.vscode/
├── package.json                    # Extension manifest
├── tsconfig.json                   # TypeScript configuration
├── .eslintrc.json                  # ESLint configuration
├── .vscodeignore                   # VSIX package exclusions
├── build.gradle                    # Gradle build integration
├── build-extension.bat             # Windows build script
├── install-extension.bat           # Windows installation script
├── language-configuration.json     # VSCode language features
├── test-sample.asciidoc            # Sample file for testing
├── src/
│   └── extension.ts               # Main extension logic
├── syntaxes/
│   └── asciidoc.tmLanguage.json   # TextMate grammar
├── server/
│   └── asciidoc-language-server-1.0.0-SNAPSHOT-standalone.jar
└── out/                           # Compiled JavaScript output
```

## Integration with Parent Build

The extension is integrated into the Gradle parent build through `settings.gradle`. The build process:

1. Builds the language server JAR in `xtextasciidocplugin.ide`
2. Copies the JAR to the extension's `server/` directory
3. Compiles TypeScript and packages the VSIX

## Next Steps

This Phase 2 implementation provides the foundation for enhanced features:

- Semantic highlighting via LSP
- More sophisticated IntelliSense
- Debugging support
- Custom commands and views
- Performance optimizations