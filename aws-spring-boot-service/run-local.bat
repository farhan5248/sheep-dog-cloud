@echo off
REM Script to run the Spring Boot application locally on Windows

REM Default configuration
set PORT=8080
set PROFILE=dev

REM Parse command line arguments
:parse_args
if "%~1"=="" goto :end_parse_args
if "%~1"=="--port" (
    set PORT=%~2
    shift
    shift
    goto :parse_args
)
if "%~1"=="--profile" (
    set PROFILE=%~2
    shift
    shift
    goto :parse_args
)
echo Unknown option: %~1
exit /b 1
:end_parse_args

echo Building application...
call mvn clean package -DskipTests

echo Running application on port %PORT% with profile %PROFILE%...
java -jar ^
  -Dserver.port=%PORT% ^
  -Dspring.profiles.active=%PROFILE% ^
  target\aws-spring-boot-service-0.0.1-SNAPSHOT.jar
