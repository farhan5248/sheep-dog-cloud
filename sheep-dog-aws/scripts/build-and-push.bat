@echo off
echo Building and pushing Docker images to GitHub Container Registry

REM Set variables
set DEPENDENCIES_IMAGE_NAME=ghcr.io/farhan5248/sheep-dog-aws-dependencies
set APP_IMAGE_NAME=ghcr.io/farhan5248/sheep-dog-aws
set VERSION=latest

echo Building Spring Boot application with Maven...
cd ..
call mvn clean package -DskipTests

echo Building dependencies Docker image...
docker build -t %DEPENDENCIES_IMAGE_NAME%:%VERSION% -f dependencies.dockerfile .

echo Building application Docker image...
docker build -t %APP_IMAGE_NAME%:%VERSION% .

echo Logging in to GitHub Container Registry...
echo Please enter your GitHub Personal Access Token when prompted
docker login ghcr.io -u farhan5248

echo Pushing dependencies Docker image to GitHub Container Registry...
docker push %DEPENDENCIES_IMAGE_NAME%:%VERSION%

echo Pushing application Docker image to GitHub Container Registry...
docker push %APP_IMAGE_NAME%:%VERSION%

echo Done!
