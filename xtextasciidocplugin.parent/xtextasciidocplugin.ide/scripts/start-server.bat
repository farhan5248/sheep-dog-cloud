@echo off
REM AsciiDoc Language Server Startup Script
REM This script starts the AsciiDoc Language Server using the standalone JAR

set SCRIPT_DIR=%~dp0
set PROJECT_ROOT=%SCRIPT_DIR%..
set JAR_PATH=%PROJECT_ROOT%\build\libs\xtextasciidocplugin.ide-1.0.0-SNAPSHOT-standalone.jar

echo Starting AsciiDoc Language Server...
echo JAR Location: %JAR_PATH%

if not exist "%JAR_PATH%" (
    echo Error: Standalone JAR not found at %JAR_PATH%
    echo Please build the project first using: gradlew fatJar
    pause
    exit /b 1
)

java -jar "%JAR_PATH%" %*