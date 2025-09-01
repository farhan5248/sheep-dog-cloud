@echo off
echo Building AsciiDoc VSCode Extension...
call npm install
call npm run compile
copy ..\xtextasciidocplugin.ide\build\libs\*-standalone.jar server\asciidoc-language-server-1.0.0-SNAPSHOT-standalone.jar
call npx vsce package --allow-missing-repository
echo Build complete!
pause