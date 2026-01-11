# UML Interaction Patterns

## Java Logging

Logging patterns specific to xtextasciidocplugin that supplement [arch-logging.md](../../../../arch-logging.md).

**Framework**: VS Code Xtext uses SLF4J directly, which supports parameterized logging with `{}` placeholders.

**When to add logging:**
- All manually edited classes that call sheep-dog-test methods must have loggers
- Generated classes (in src-gen/ and xtext-gen/) do NOT have loggers
- Methods calling sheep-dog-test business logic use entry/exit debug logging

**When entry/exit logging is required:**
ALL methods that call sheep-dog-test business logic must include entry/exit debug logging for tracing execution flow. This includes:
- Methods that directly call `*IssueDetector` or `*IssueResolver` classes
- Methods that call `createAcceptor()` which delegates to `*Resolver` classes

### Logger Initialization

Standard SLF4J logger initialization in each class:

**Example**
```java
private static final Logger logger = LoggerFactory.getLogger({ClassName}.class);
```

### Entry/Exit Logging Pattern

Methods that delegate to sheep-dog-test business logic use entry/exit debug logging.

See [arch-logging.md](../../../../arch-logging.md) "SLF4J Projects (Parameterized Logging)" section for patterns.

**Example**
```java
public void check{Type}{Aspect}({Type} element) {
    logger.debug("check{Type}{Aspect} entry: {}", element.getName());
    // ... business logic delegation
    logger.debug("check{Type}{Aspect} exit");
}
```

### Error Logging Pattern

See [arch-logging.md](../../../../arch-logging.md) "Error Logging Pattern (SLF4J)" section.

**Example**
```java
try {
    // ... business logic
} catch (Exception e) {
    logger.error("check{Type}{Aspect} failed for: {}", element.getName(), e);
}
```

## Java Exceptions

Exception handling for Xtext Language Server integration. See [arch-xtext-logging.md](../../../../arch-xtext-logging.md) "Xtext IDE Exception Handling" section for rationale and patterns.

## TypeScript Logging

Logging patterns for the VS Code extension (xtextasciidocplugin.vscode).

**Framework**: VS Code extensions use console logging and VS Code Output Channels.

**When to add logging:**
- Extension activation/deactivation
- Language server lifecycle events (start, stop, restart)
- Configuration changes
- Error conditions

### Console Logging Pattern

Use console methods for development debugging:

**Example**
```typescript
console.log('Extension activated');
console.debug('Server started with options:', options);
console.error('Failed to start server:', error);
```

### VS Code Output Channel Pattern

Use Output Channels for user-visible logging:

**Example**
```typescript
const outputChannel = vscode.window.createOutputChannel('AsciiDoc');
outputChannel.appendLine('Language server started');
outputChannel.show(); // Show output panel on errors
```

### Async Error Handling Pattern

Handle errors in async/await code:

**Example**
```typescript
async function startServer(): Promise<void> {
    try {
        await client.start();
        console.log('Language client started');
    } catch (error) {
        console.error('Failed to start language client:', error);
        vscode.window.showErrorMessage('Failed to start AsciiDoc language server');
    }
}
```

### Promise Error Handling Pattern

Handle errors in Promise chains:

**Example**
```typescript
client.sendRequest('custom/command', params)
    .then(result => {
        console.debug('Command result:', result);
    })
    .catch(error => {
        console.error('Command failed:', error);
    });
```

## TypeScript Exceptions

**Activation Errors**: Catch and log errors during extension activation to prevent silent failures.

**Example**
```typescript
export async function activate(context: vscode.ExtensionContext): Promise<void> {
    try {
        // ... activation logic
    } catch (error) {
        console.error('Extension activation failed:', error);
        throw error; // Re-throw to signal activation failure to VS Code
    }
}
```

**Deactivation Cleanup**: Ensure cleanup happens even if errors occur.

**Example**
```typescript
export async function deactivate(): Promise<void> {
    if (client) {
        try {
            await client.stop();
        } catch (error) {
            console.error('Error stopping client:', error);
        }
    }
}
```
