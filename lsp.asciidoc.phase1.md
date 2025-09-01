# AsciiDoc Language Server - Phase 1 Implementation

## Overview

This document describes the Phase 1 implementation of the AsciiDoc Language Server enhancement, which adds Language Server Protocol (LSP) support to the existing AsciiDoc Xtext project (`xtextasciidocplugin.parent`).

## Phase 1 Goals

### 1.1 IDE Module Enhancement

**Enhanced LSP4J Dependencies**
- Add LSP4J 0.22.0 dependencies to `xtextasciidocplugin.ide/build.gradle`
- Add `org.eclipse.lsp4j:org.eclipse.lsp4j:0.22.0`
- Add `org.eclipse.lsp4j:org.eclipse.lsp4j.jsonrpc:0.22.0`

**Custom Server Launcher**
- Create `AsciiDocServerLauncher.java` extending Xtext's `ServerLauncher`
- Support both stdio and socket communication modes
- Command line arguments: 
  - No arguments: stdio communication (default)
  - `-socket PORT`: socket communication (fallback to stdio for now)

**Language Server Module**
- Create `AsciiDocLanguageServerModule.java` extending `ServerModule`
- Provide foundation for future custom LSP behavior
- Use default Xtext language server configuration

**Distribution Configuration**
- Add fat JAR generation with all dependencies included
- Create standalone JAR: `asciidoc-language-server-1.0.0-SNAPSHOT-standalone.jar`
- Add distribution ZIP with launch scripts
- Fix signature file conflicts for proper JAR execution

### 1.2 Server Testing & Validation

**Command Line Testing**
- Verify language server starts successfully via Gradle: `./gradlew :xtextasciidocplugin.ide:run`
- Test standalone JAR runs correctly: `java -jar asciidoc-language-server-1.0.0-SNAPSHOT-standalone.jar`
- Validate socket parameter handling: `-socket 8080` (with fallback message)
- Confirm custom startup messages show our launcher is active

**LSP Infrastructure Validation**
- Verify generated Xtext language artifacts (parser, content assist, validation)
- Ensure IDE module classes generated correctly
- Confirm LSP4J dependencies integrated without conflicts
- Test distribution ZIP creation (expected size with all dependencies)

## Implementation Plan

### Key Files to Create/Modify:

**Build Configuration:**
- `C:\Users\Farhan\git\sheep-dog-main\sheep-dog-cloud\xtextasciidocplugin.parent\xtextasciidocplugin.ide\build.gradle`

**Language Server Implementation:**
- `C:\Users\Farhan\git\sheep-dog-main\sheep-dog-cloud\xtextasciidocplugin.parent\xtextasciidocplugin.ide\src\main\java\org\farhan\dsl\asciidoc\ide\AsciiDocServerLauncher.java`
- `C:\Users\Farhan\git\sheep-dog-main\sheep-dog-cloud\xtextasciidocplugin.parent\xtextasciidocplugin.ide\src\main\java\org\farhan\dsl\asciidoc\ide\AsciiDocLanguageServerModule.java`

**Launch Scripts:**
- `C:\Users\Farhan\git\sheep-dog-main\sheep-dog-cloud\xtextasciidocplugin.parent\xtextasciidocplugin.ide\scripts\start-server.bat`

**Test Files:**
- `C:\Users\Farhan\git\sheep-dog-main\sheep-dog-cloud\xtextasciidocplugin.parent\test.asciidoc`

### Build.gradle Enhancements

Add the following dependencies to `xtextasciidocplugin.ide/build.gradle`:

```gradle
dependencies {
    // Existing dependencies...
    
    // LSP4J dependencies for Language Server Protocol
    api 'org.eclipse.lsp4j:org.eclipse.lsp4j:0.22.0'
    api 'org.eclipse.lsp4j:org.eclipse.lsp4j.jsonrpc:0.22.0'
}

// Fat JAR configuration
task fatJar(type: Jar) {
    archiveClassifier = 'standalone'
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from { configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) } }
    with jar
    exclude 'META-INF/*.RSA', 'META-INF/*.SF', 'META-INF/*.DSA'
    
    manifest {
        attributes 'Main-Class': 'org.farhan.dsl.asciidoc.ide.AsciiDocServerLauncher'
    }
}

// Distribution configuration
distributions {
    main {
        contents {
            from(fatJar)
            from('scripts') {
                into 'bin'
                fileMode = 0755
            }
        }
    }
}

// Run configuration
run {
    mainClass = 'org.farhan.dsl.asciidoc.ide.AsciiDocServerLauncher'
    standardInput = System.in
}
```

### AsciiDocServerLauncher.java

```java
package org.farhan.dsl.asciidoc.ide;

import org.eclipse.xtext.ide.server.ServerLauncher;
import com.google.inject.Injector;
import org.farhan.dsl.asciidoc.AsciiDocStandaloneSetup;

public class AsciiDocServerLauncher extends ServerLauncher {
    
    public static void main(String[] args) {
        System.out.println("Starting AsciiDoc Language Server...");
        
        if (args.length > 0 && "-socket".equals(args[0])) {
            System.out.println("Socket mode requested but not yet implemented, falling back to stdio");
        }
        
        new AsciiDocServerLauncher().run(args);
    }
    
    @Override
    protected Injector createInjector() {
        return new AsciiDocStandaloneSetup().createInjectorAndDoEMFRegistration();
    }
}
```

### AsciiDocLanguageServerModule.java

```java
package org.farhan.dsl.asciidoc.ide;

import org.eclipse.xtext.ide.server.ServerModule;

public class AsciiDocLanguageServerModule extends ServerModule {
    
    // Default implementation for now
    // Future customizations can be added here
}
```

## Usage Instructions

### Starting the Language Server

**Via Gradle (Development):**
```bash
cd "C:\Users\Farhan\git\sheep-dog-main\sheep-dog-cloud\xtextasciidocplugin.parent"
./gradlew :xtextasciidocplugin.ide:run
```

**Via Standalone JAR:**
```bash
cd "C:\Users\Farhan\git\sheep-dog-main\sheep-dog-cloud\xtextasciidocplugin.parent\xtextasciidocplugin.ide\build\libs"
java -jar asciidoc-language-server-1.0.0-SNAPSHOT-standalone.jar
```

**Via Batch Script (Windows):**
```batch
cd "C:\Users\Farhan\git\sheep-dog-main\sheep-dog-cloud\xtextasciidocplugin.parent\xtextasciidocplugin.ide\scripts"
start-server.bat
```

### Building the Language Server

**Build Everything:**
```bash
./gradlew build
```

**Build Just the Fat JAR:**
```bash
./gradlew :xtextasciidocplugin.ide:fatJar
```

**Build Distribution ZIP:**
```bash
./gradlew :xtextasciidocplugin.ide:distZip
```

## Current Language Server Features

- **Syntax Validation**: AsciiDoc grammar validation via Xtext
- **Content Assistance**: Auto-completion for AsciiDoc language constructs
- **Error Reporting**: Real-time error diagnostics
- **LSP Communication**: Stdio-based communication (socket support planned)

## AsciiDoc Grammar Support

The language server supports the current AsciiDoc grammar:
```
Model: greetings+=Greeting*;
Greeting: 'Hello' name=ID '!';
```

**Example AsciiDoc file (test.asciidoc):**
```
Hello World!
Hello Xtext!
Hello Language Server!
Hello AsciiDoc!
```

## Testing Plan

### Test Cases
1. **Basic Functionality**
   - Language server starts without errors
   - Accepts LSP initialization requests
   - Processes document open/close notifications

2. **Grammar Validation**
   - Valid Hello statements parse correctly
   - Invalid syntax generates appropriate errors
   - Content assist provides completions

3. **JAR Distribution**
   - Standalone JAR runs independently
   - All dependencies included properly
   - No classpath conflicts

### Test Files
Create sample AsciiDoc files with:
- Valid Hello statements
- Invalid syntax (missing exclamation, wrong keywords)
- Mixed content scenarios

## Expected Artifacts

After Phase 1 completion:

**Generated Files:**
- `asciidoc-language-server-1.0.0-SNAPSHOT-standalone.jar` (~15-20MB with dependencies)
- Distribution ZIP with launch scripts
- Generated Xtext artifacts (parser, validator, etc.)

**Source Files:**
- `AsciiDocServerLauncher.java`
- `AsciiDocLanguageServerModule.java`
- Enhanced `build.gradle` with LSP dependencies
- Windows batch script for server startup

## Success Criteria

Phase 1 is complete when:
- ✅ Enhanced IDE module with LSP4J dependencies
- ✅ Custom server launcher with stdio/socket modes
- ✅ Standalone JAR distribution
- ✅ Command line startup validation
- ✅ LSP infrastructure confirmation

## Next Steps (Phase 2)

1. **VSCode Extension Creation**
   - Create TypeScript-based language client
   - Implement file association for .asciidoc files
   - Add TextMate grammar for syntax highlighting

2. **Socket Communication**
   - Complete socket communication implementation
   - Add proper multi-client support

3. **Advanced LSP Features**
   - Semantic syntax highlighting
   - Go to definition
   - Find references
   - Document formatting

## Technical Details

- **Xtext Version**: 2.39.0 (same as MyDsl)
- **LSP4J Version**: 0.22.0
- **Java Version**: 21
- **Build System**: Gradle 8.14
- **Platform**: Windows (with cross-platform JAR)
- **Package**: `org.farhan.dsl.asciidoc`
- **Grammar**: Currently simple Hello/Greeting pattern

## Verification Steps

1. Build the project: `./gradlew build`
2. Run via Gradle: `./gradlew :xtextasciidocplugin.ide:run`
3. Test standalone JAR: `java -jar asciidoc-language-server-*-standalone.jar`
4. Create test .asciidoc file and validate parsing
5. Check LSP communication by monitoring stdin/stdout

The language server will be ready for Phase 2 VSCode extension development upon successful completion of these tasks.