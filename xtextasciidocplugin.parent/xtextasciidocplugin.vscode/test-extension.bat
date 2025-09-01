@echo off
echo AsciiDoc VSCode Extension - Phase 3 Test Script
echo =================================================

REM Compile the TypeScript extension
echo Compiling extension...
call npm run compile
if %errorlevel% neq 0 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo.
echo Extension compiled successfully!
echo.

echo Phase 3 Features to Test:
echo --------------------------
echo 1. Generate AsciiDoc Boilerplate (Ctrl+Alt+G)
echo 2. Generate Tests from AsciiDoc (Ctrl+Alt+T)
echo 3. Format AsciiDoc Document (Shift+Alt+F)
echo 4. Validate All AsciiDoc Files (Ctrl+Alt+V)
echo.

echo Test Files:
echo - example.asciidoc (sample file with Hello statements)
echo - Custom icon: icons/asciidoc-icon.svg
echo.

echo Instructions:
echo 1. Open VSCode in this directory
echo 2. Press F5 to launch Extension Development Host
echo 3. Open example.asciidoc in the new window
echo 4. Try each command using Ctrl+Shift+P or keybindings
echo 5. Test context menus (right-click in editor and Explorer)
echo.

echo Configuration Settings:
echo - asciidoc.languageServer.enabled
echo - asciidoc.languageServer.port
echo - asciidoc.formatting.enabled
echo - asciidoc.validation.enabled  
echo - asciidoc.boilerplate.template
echo.

echo Ready to test! Press F5 in VSCode to start testing.
pause