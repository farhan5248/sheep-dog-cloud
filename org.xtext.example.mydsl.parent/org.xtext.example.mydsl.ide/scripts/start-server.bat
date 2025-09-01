@echo off
echo Starting MyDsl Language Server...
cd /d "%~dp0..\build\libs"
java -jar mydsl-language-server-1.0.0-SNAPSHOT-standalone.jar %*