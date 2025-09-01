# Comprehensive VSCode Extension Plan for AsciiDoc Project

## Project Analysis Summary

**Current State:**
- AsciiDoc Xtext language (`xtextasciidocplugin.parent`)
- Grammar: Basic "Hello name!" greeting language (placeholder structure)
- Package: `org.farhan.dsl.asciidoc`
- Xtext 2.39.0 with Java 21 support
- Gradle build system with existing IDE module
- Language server launcher configuration needed in `xtextasciidocplugin.ide`

## Conversion Strategy: Extend Existing Project

**Recommended Approach:** Add new VSCode extension module rather than restructuring existing project.

### Phase 1: Language Server Enhancement (Week 1)

**1.1 Enhance IDE Module for LSP**
- Add LSP4J dependencies to `xtextasciidocplugin.ide/build.gradle`
- Create custom `AsciiDocServerLauncher.java` extending `ServerLauncher`
- Configure socket and stdio communication modes
- Add distribution configuration for standalone server JAR

**1.2 Server Testing & Validation**
- Test language server startup via command line
- Validate LSP communication with test client
- Ensure all language features work (syntax highlighting, completion, validation)

### Phase 2: VSCode Extension Creation (Week 1-2)

**2.1 Extension Module Structure**
```
xtextasciidocplugin.vscode/
├── package.json              # Extension manifest
├── src/
│   └── extension.ts          # Main extension activation
├── syntaxes/
│   └── asciidoc.tmLanguage.json # TextMate grammar for syntax highlighting
├── language-configuration.json # Language config (brackets, comments)
└── server/                   # Language server distribution
    └── asciidoc-language-server.jar
```

**2.2 Extension Implementation**
- Initialize with Yeoman VSCode generator as baseline
- Configure TypeScript-based language client
- Implement server startup logic (embedded JAR approach)
- Set up file association (.asciidoc extension)
- Create TextMate grammar for basic syntax highlighting

**2.3 Build Integration**
- Add VSCode module to parent `settings.gradle`
- Create npm build scripts in extension module
- Configure Gradle task to copy language server JAR
- Set up VSIX packaging for distribution

### Phase 3: Advanced Features (Week 2-3)

**3.1 Language Server Features**
- Semantic syntax highlighting via LSP
- Content completion and IntelliSense
- Error diagnostics and validation
- Go to definition and reference finding
- Document formatting and outline

**3.2 Extension Features**
- Custom file icons for .asciidoc files
- Command palette integration
- Configuration settings for language server
- Debug and logging capabilities

**3.3 Custom Commands Integration**
- **Code Generation Commands:**
  - "Generate AsciiDoc Boilerplate" - Creates template AsciiDoc file with standard structure
  - "Generate Tests from AsciiDoc" - Auto-generate test cases based on AsciiDoc content
- **Language Operations Commands:**
  - "Format AsciiDoc Document" - Custom formatting beyond standard LSP formatting
  - "Validate All AsciiDoc Files" - Batch validation across entire workspace

### Phase 4: Testing & Distribution (Week 3-4)

**4.1 Comprehensive Testing**
- Unit tests for language server components
- Integration tests with VSCode extension host
- End-to-end testing with sample AsciiDoc files
- Cross-platform testing (Windows primary)

**4.2 Distribution Setup**
- VSIX package generation pipeline
- Local installation testing
- Documentation and usage examples
- GitHub releases or marketplace preparation

## Technical Architecture

### Language Server Architecture
- **Base:** Existing `xtextasciidocplugin.ide` module  
- **Communication:** Socket-based LSP (more reliable than stdio)
- **Packaging:** Fat JAR with all dependencies included
- **Deployment:** Embedded within VSCode extension

### VSCode Extension Architecture  
- **Language Client:** TypeScript with `vscode-languageclient`
- **Server Management:** Extension manages server lifecycle
- **File Types:** `.asciidoc` file association
- **Syntax:** TextMate grammar + LSP semantic highlighting

### Build Coordination
- **Gradle:** Language server and Java components
- **npm/TypeScript:** VSCode extension client
- **Integration:** Gradle copies server JAR to extension package

## Implementation Details

### Key Files to Create/Modify:

**Language Server Side:**
- `xtextasciidocplugin.ide/build.gradle` (enhance LSP dependencies)
- `AsciiDocServerLauncher.java` (custom server launcher)
- `AsciiDocLanguageServerModule.java` (LSP configuration)

**VSCode Extension Side:**
- `xtextasciidocplugin.vscode/package.json` (extension manifest)
- `xtextasciidocplugin.vscode/src/extension.ts` (main extension)
- `xtextasciidocplugin.vscode/syntaxes/asciidoc.tmLanguage.json` (syntax)

**Build Configuration:**
- Update parent `settings.gradle` to include VSCode module
- Create `xtextasciidocplugin.vscode/build.gradle` for integration
- Add npm scripts and TypeScript configuration

### Dependencies Required:
- **Language Server:** `org.eclipse.lsp4j` (LSP implementation)
- **VSCode Extension:** `vscode-languageclient`, `@types/vscode`
- **Build Tools:** Node.js, npm, TypeScript compiler

## Success Criteria

1. **Functional Language Server:** Responds to LSP requests correctly
2. **Working Extension:** Installs and activates in VSCode  
3. **Language Features:** Syntax highlighting, validation, completion work
4. **Cross-platform:** Works on Windows (primary) and other platforms
5. **Maintainable:** Clean separation between server and client
6. **Documented:** Clear setup and usage instructions

## Risk Mitigation

- **Version Compatibility:** Use proven Xtext 2.39.0 + LSP4J combinations
- **Build Complexity:** Leverage existing successful examples (cdietrich, TypeFox)
- **Testing:** Implement comprehensive test suite early
- **Windows Focus:** Prioritize Windows compatibility per environment requirements

## Project-Specific Adaptations

### Package Structure
- **Base Package:** `org.farhan.dsl.asciidoc` (instead of `org.xtext.example.mydsl`)
- **Module Names:** `xtextasciidocplugin` and `xtextasciidocplugin.ide`
- **File Extension:** `.asciidoc` (instead of `.mydsl`)
- **Grammar Name:** `AsciiDoc.xtext`

### Integration Considerations
- **Sheep-Dog Ecosystem:** Ensure compatibility with existing sheep-dog transformation pipeline
- **AsciiDoc Standards:** Align with standard AsciiDoc syntax and conventions
- **Cloud Environment:** Consider deployment within sheep-dog-cloud microservices context

This plan leverages the existing AsciiDoc foundation while following proven patterns from successful Xtext VSCode extensions, adapted specifically for the sheep-dog ecosystem requirements.