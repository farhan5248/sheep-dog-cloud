# UML Interaction Patterns

## Java Logging

Logging patterns specific to xtextasciidocplugin that supplement [arch-logging.md](../../../../arch-logging.md).

**Framework**: VS Code Xtext uses SLF4J directly, which supports parameterized logging with `{}` placeholders.

See [arch-xtext-logging.md](../../../../arch-xtext-logging.md) "When to Add Logging" section for when to add logging and entry/exit logging requirements.

### Logger Initialization

Standard SLF4J logger initialization in each class:

**Example**
```java
private static final Logger logger = LoggerFactory.getLogger({ClassName}.class);
```

### Entry/Exit Logging Pattern

Methods that delegate to sheep-dog-test business logic use entry/exit debug logging.

See [impl-slf4j.md](../../../../impl-slf4j.md) for entry, exit, and error logging patterns.

### Error Logging Pattern

See [impl-slf4j.md](../../../../impl-slf4j.md) "Error Logging Pattern" section.

## Java Exceptions

Exception handling for Xtext Language Server integration. See [arch-xtext-logging.md](../../../../arch-xtext-logging.md) "Xtext IDE Exception Handling" section for rationale and patterns.

## TypeScript Logging

Logging patterns for the VS Code extension (xtextasciidocplugin.vscode).

**Framework**: VS Code OutputChannel API (built-in, no external library)

See [arch-xtext-logging.md](../../../../arch-xtext-logging.md) "VS Code Extension Logging" section for when to add logging.

### OutputChannel Initialization

**Example**
```typescript
this.outputChannel = vscode.window.createOutputChannel(constants.OUTPUT_CHANNELS.EXTENSION);
```

### Entry/Exit Logging Pattern

Methods use entry/exit logging following the same pattern as Java.

See [impl-vscode-outputchannel.md](../../../../impl-vscode-outputchannel.md) for entry, exit, and error logging patterns.

### Error Logging Pattern

See [impl-vscode-outputchannel.md](../../../../impl-vscode-outputchannel.md) "Error Logging Pattern" section.

## TypeScript Exceptions

Exception handling for VS Code extension. See [arch-xtext-logging.md](../../../../arch-xtext-logging.md) "VS Code Extension Exception Handling" section for patterns.
