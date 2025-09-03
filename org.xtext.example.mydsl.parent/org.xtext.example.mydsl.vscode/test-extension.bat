@echo off
echo Testing MyDsl VSCode Extension Phase 3 Features
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
echo    - mydsl.generateBoilerplate (Ctrl+Alt+G)
echo    - mydsl.generateTests (Ctrl+Alt+T) 
echo    - mydsl.formatDocument (Shift+Alt+F)
echo    - mydsl.validateAllFiles (Ctrl+Alt+V)
echo    - mydsl.helloWorld
echo.

echo 4. Test files created:
echo    - example.mydsl (sample file to test commands)
echo    - icons/mydsl-icon.svg (custom file icon)
echo.

echo 5. To test the extension:
echo    a. Open VSCode in this directory
echo    b. Press F5 to launch Extension Development Host
echo    c. Open example.mydsl in the new window
echo    d. Use Ctrl+Shift+P to access the command palette
echo    e. Type "MyDsl" to see all available commands
echo.

echo Extension is ready for testing!
pause