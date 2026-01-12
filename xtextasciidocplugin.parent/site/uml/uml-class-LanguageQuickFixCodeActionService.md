# {Language}QuickFixCodeActionService

## {Language}QuickFixCodeActionService extends QuickFixCodeActionService

Extends Xtext's QuickFixCodeActionService to provide code actions via Language Server Protocol.

Bridges LSP code action requests to the IdeQuickfixProvider.

**Examples**

```java
public class {Language}QuickFixCodeActionService extends QuickFixCodeActionService
```

## getCodeActions method handles LSP CodeAction requests

Receives code action requests from the VS Code client and delegates to IdeQuickfixProvider.

Returns a list of CodeAction objects that VS Code displays as quick fixes.

**Examples**

```java
@Override
public List<Either<Command, CodeAction>> getCodeActions(ICodeActionService2.Options options)
```
