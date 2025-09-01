@echo off
echo Testing AsciiDoc VSCode Extension Phase 3 Features
echo ================================================
echo.

echo 1. Compiling TypeScript...
call npm run compile
if errorlevel 1 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo.
echo 2. Extension compiled successfully!
echo.

echo 3. Available commands in the extension:
echo    - asciidoc.generateBoilerplate (Ctrl+Alt+G)
echo    - asciidoc.generateTests (Ctrl+Alt+T) 
echo    - asciidoc.formatDocument (Shift+Alt+F)
echo    - asciidoc.validateAllFiles (Ctrl+Alt+V)
echo    - asciidoc.helloWorld
echo.

echo 4. Test files created:
echo    - example.asciidoc (sample file to test commands)
echo    - icons/asciidoc-icon.svg (custom file icon)
echo.

echo 5. To test the extension:
echo    a. Open VSCode in this directory
echo    b. Press F5 to launch Extension Development Host
echo    c. Open example.asciidoc in the new window
echo    d. Use Ctrl+Shift+P to access the command palette
echo    e. Type "AsciiDoc" to see all available commands
echo.

echo Extension is ready for testing!
pause