@echo off
echo Building AsciiDoc VSCode Extension...

echo.
echo Step 1: Installing npm dependencies...
call npm install
if %errorlevel% neq 0 (
    echo ERROR: npm install failed!
    exit /b %errorlevel%
)

echo.
echo Step 2: Compiling TypeScript...
call npm run compile
if %errorlevel% neq 0 (
    echo ERROR: TypeScript compilation failed!
    exit /b %errorlevel%
)

echo.
echo Step 3: Ensuring language server JAR is present...
if not exist "server\xtextasciidocplugin.ide-1.0.0-SNAPSHOT-standalone.jar" (
    echo Copying language server JAR...
    if not exist "server" mkdir server
    copy "..\xtextasciidocplugin.ide\build\libs\xtextasciidocplugin.ide-1.0.0-SNAPSHOT-standalone.jar" "server\" > nul
    if %errorlevel% neq 0 (
        echo ERROR: Failed to copy language server JAR!
        echo Make sure the language server has been built first with: gradlew build
        exit /b %errorlevel%
    )
)

echo.
echo Step 4: Creating VSIX package...
call npx vsce package --allow-missing-repository
if %errorlevel% neq 0 (
    echo ERROR: VSIX packaging failed!
    exit /b %errorlevel%
)

echo.
echo SUCCESS: AsciiDoc VSCode Extension built successfully!
for %%f in (*.vsix) do echo Package created: %%f
echo.
echo To install the extension in VS Code, run: install-extension.bat
pause
