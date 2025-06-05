@echo off
echo Building and pushing Docker image to GitHub Container Registry

REM Set variables
set IMAGE_NAME=ghcr.io/farhan5248/aws-hello-world-cline
set VERSION=latest

echo Building Spring Boot application with Maven...
cd ..
call mvn clean package -DskipTests

echo Building Docker image...
docker build -t %IMAGE_NAME%:%VERSION% .

echo Logging in to GitHub Container Registry...
echo Please enter your GitHub Personal Access Token when prompted
docker login ghcr.io -u farhan5248

echo Pushing Docker image to GitHub Container Registry...
docker push %IMAGE_NAME%:%VERSION%

echo Done!
