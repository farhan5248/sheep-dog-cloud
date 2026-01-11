# UML Communication Patterns

Collaboration patterns for xtextasciidocplugin IDE integration via Language Server Protocol. Business logic patterns are documented in sheep-dog-test/site/uml/uml-communication.md.

## IDE Integration (Java - Language Server)

This collaboration applies when the Xtext language server needs to validate, propose, or correct grammar elements. IDE integration classes wrap EMF objects and delegate to business logic. Business logic classes (Detector/Resolver) are in sheep-dog-test, not xtextasciidocplugin.

**Methods**
- Grammar element validation workflows
- Content assist proposal generation
- Quick fix correction workflows
- EMF object wrapping and delegation

### {Language}Validator

Validates grammar elements in the language server by wrapping EMF objects and delegating to business logic detectors.

**Methods**
- `check{Type}{Aspect}({Type} eObject)`
- `initProject(Resource resource)`
- `logError(Exception e, String name)`

### {Language}IdeContentProposalProvider

Provides content assist proposals via LSP by delegating to business logic resolvers.

**Methods**
- `complete{Type}_{Assignment}({TypeClass} model, Assignment, ContentAssistContext, IIdeContentProposalAcceptor acceptor)`
- `complete{Assignment}({Type} model, Assignment, ContentAssistContext, IIdeContentProposalAcceptor acceptor)`
- `initProject(Resource resource)`

### {Language}IdeQuickfixProvider

Provides quick fix corrections via LSP by delegating to business logic resolvers and applying modifications to EMF objects.

**Methods**
- `fix{Type}{Aspect}(Issue issue, IssueResolutionAcceptor acceptor)`
- `createAcceptor(Issue issue, IssueResolutionAcceptor acceptor, ArrayList<{Language}IssueProposal> proposals)`
- `getEObject(Issue issue)`

### {Language}QuickFixCodeActionService

Bridges LSP CodeAction requests to IdeQuickfixProvider.

**Methods**
- `getCodeActions(ICodeActionService2.Options options)`

### {Type}Impl

Wrapper class that adapts EMF objects to business logic interfaces by delegating attribute access.

**Methods**
- `{Type}Impl({Type} eObject)`
- `get{Assignment}()`
- `getParent()`

### {Type}IssueDetector

Business logic class from sheep-dog-test (not in xtextasciidocplugin) that provides pure validation logic for grammar elements.

**Methods**
- `validate{Aspect}(I{Type} type)`

### {Type}IssueResolver

Business logic class from sheep-dog-test (not in xtextasciidocplugin) that generates proposals and corrections for grammar elements.

**Methods**
- `suggest{Assignment}(I{Type} type)`
- `correct{Aspect}(I{Type} type)`

### {Language}Factory

Singleton from sheep-dog-test (not in xtextasciidocplugin) that creates and manages project instances.

**Methods**
- `createTestProject()`

### {Language}IssueProposal

Data container from sheep-dog-test (not in xtextasciidocplugin) that holds proposal information for content assist and quick fixes.

**Properties:** id, description, value, qualifiedName

## VS Code Client Integration (TypeScript)

This collaboration applies when VS Code needs to communicate with the language server.

### extension

Entry point that initializes the language client and manages its lifecycle.

**Methods**
- `activate(context)` - Start language client
- `deactivate()` - Stop language client

### serverLauncher

Spawns and manages the Java language server process.

**Methods**
- `startServer()` - Launch language server
- `getServerOptions()` - Configure server connection

### communicationService

Handles custom LSP communication beyond standard protocol.

**Methods**
- `sendRequest(client, method, params)` - Send custom requests
- `registerNotificationHandlers(client)` - Register notification listeners

### configurationService

Manages VS Code extension settings.

**Methods**
- `getConfiguration()` - Read settings
- `onConfigurationChanged(event)` - Handle settings updates
