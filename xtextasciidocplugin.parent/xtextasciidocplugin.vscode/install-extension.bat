@echo off
echo Installing AsciiDoc VSCode Extension...

echo.
echo Looking for VSIX package...
for %%f in (*.vsix) do (
    echo Found: %%f
    echo Installing extension to VS Code...
    code --install-extension "%%f"
    if %errorlevel% equ 0 (
        echo.
        echo SUCCESS: Extension installed successfully!
        echo.
        echo Next steps:
        echo 1. Restart VS Code if it's currently running
        echo 2. Open a .asciidoc file to test the extension
        echo 3. Check the extension is activated in VS Code Extensions view
        echo.
        echo Sample AsciiDoc content:
        echo   Hello World!
        echo   Hello VSCode!
        echo.
    ) else (
        echo ERROR: Failed to install extension!
        echo Make sure VS Code is installed and 'code' command is available in PATH
    )
    goto :end
)

echo ERROR: No .vsix file found in current directory!
echo Run build-extension.bat first to create the package.

:end
pause
