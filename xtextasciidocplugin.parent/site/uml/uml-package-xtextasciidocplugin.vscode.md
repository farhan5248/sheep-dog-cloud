# UML Package Patterns

## xtextasciidocplugin.vscode/src

### extension

- Main VS Code extension entry point
- Activates/deactivates the language client

**Examples:**
- extension.ts

### serverLauncher

- Launches the Xtext language server
- Manages server process lifecycle

**Examples:**
- serverLauncher.ts

### communicationService

- Client-server communication via LSP
- Handles custom commands and notifications

**Examples:**
- communicationService.ts

### configurationService

- Configuration management for VS Code settings
- Reads and applies extension settings

**Examples:**
- configurationService.ts

### constants

- Constants and configuration values
- Language IDs, file extensions, command names

**Examples:**
- constants.ts
