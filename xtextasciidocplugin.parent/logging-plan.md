# Logging Enhancement Plan for Xtext VSCode Extension

## **Current State Issues**
- Java server uses primitive `System.out.println()` instead of proper logging
- No logging in critical methods like `doGenerateResource` 
- Missing traceability for validation, code actions, and diagnostic events
- TypeScript client has good logging foundation but needs enhancement

## **Phase 1: Java Server Logging Infrastructure**
1. Add SLF4J + Logback dependencies to `build.gradle`
2. Create `logback.xml` configuration with file rotation
3. Replace all `System.out.println()` calls with proper logger statements
4. Add structured logging to `AsciidocGenerator.doGenerateResource()` method

## **Phase 2: Method-Level Tracing**  
5. Add entry/exit logging to all public methods in custom Java classes
6. Add detailed logging to validation workflows
7. Log all command executions and their parameters

## **Phase 3: TypeScript Client Enhancements**
8. Add request/response logging for LSP communication
9. Add logging level configuration to `configuration.json`

## **Phase 4: Configuration & Documentation**
10. Document log file locations and viewing methods
11. Add examples for tracing specific method calls

## **Expected Outcome**
- **doGenerateResource tracing**: See method entry, parameters, execution time, and results
- **Log locations**: VSCode Output panel + rotating files in workspace/.vscode/logs/
- **Level control**: Runtime switching between INFO/WARN/DEBUG via commands
- **Debug visibility**: Complete request/response flow tracing for all LSP operations

## **Implementation Progress**
- [x] Step 1: Add SLF4J + Logback dependencies to `build.gradle`
- [x] Step 2: Create `logback.xml` configuration with file rotation
- [x] Step 3: Replace all `System.out.println()` calls with proper logger statements
- [x] Step 4: Add entry/exit debug logging to all public methods in Java classes or classes that use the logger
- [x] Step 5: Add structured logging to `AsciidocGenerator`, `AsciiDocIdeContentProposalProvider`, `AsciiDocIdeQuickfixProvider`, `AsciiDocValidator` classes, focus on try catch blocks
- [x] Step 6: Add detailed logging to `AsciidocGenerator`, `AsciiDocIdeContentProposalProvider`, `AsciiDocIdeQuickfixProvider`, `AsciiDocValidator` classes workflows
- [x] Step 7: Log all command executions and their parameters
- [x] Step 8: Add request/response logging for LSP communication
- [x] Step 9: Add logging level configuration to `configuration.json`
- [ ] Step 10: Document log file locations and viewing methods
- [ ] Step 11: Add examples for tracing specific method calls