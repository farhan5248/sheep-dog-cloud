@echo off
REM Script to run the Spring Boot application using Docker Compose on Windows

REM Default configuration
set ACTION=up

REM Parse command line arguments
:parse_args
if "%~1"=="" goto :end_parse_args
if "%~1"=="--action" (
    set ACTION=%~2
    shift
    shift
    goto :parse_args
)
echo Unknown option: %~1
exit /b 1
:end_parse_args

REM Check if Docker is running
docker info > nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo Docker is not running. Please start Docker and try again.
    exit /b 1
)

REM Build the application
if "%ACTION%"=="up" (
    goto :build_app
) else if "%ACTION%"=="build" (
    goto :build_app
) else (
    goto :skip_build
)

:build_app
echo Building application...
call mvn clean package -DskipTests

:skip_build
REM Perform the requested Docker Compose action
if "%ACTION%"=="up" (
    echo Starting containers...
    docker-compose up -d
    echo Application is running at http://localhost:8080
) else if "%ACTION%"=="down" (
    echo Stopping containers...
    docker-compose down
) else if "%ACTION%"=="build" (
    echo Building Docker image...
    docker-compose build
) else if "%ACTION%"=="logs" (
    echo Showing logs...
    docker-compose logs -f
) else if "%ACTION%"=="restart" (
    echo Restarting containers...
    docker-compose restart
) else (
    echo Unknown action: %ACTION%
    echo Supported actions: up, down, build, logs, restart
    exit /b 1
)

echo Done!
