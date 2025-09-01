@echo off
echo Installing AsciiDoc VSCode Extension...
for %%f in (*.vsix) do (
    echo Installing %%f
    code --install-extension "%%f"
    echo Extension installed!
    goto end
)
echo No VSIX file found!
:end
pause