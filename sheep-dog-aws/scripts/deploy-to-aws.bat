@echo off
echo Deploying Spring Boot service to AWS ECS

REM Set variables
set STACK_NAME=sheep-dog-aws
set REGION=us-east-1
set IMAGE_URL=ghcr.io/farhan5248/sheep-dog-aws:latest

echo Checking if AWS CLI is installed...
aws --version
if %ERRORLEVEL% neq 0 (
    echo AWS CLI is not installed. Please install it first.
    exit /b 1
)

echo Checking if you are logged in to AWS...
aws sts get-caller-identity
if %ERRORLEVEL% neq 0 (
    echo You are not logged in to AWS. Please run 'aws configure' first.
    exit /b 1
)

echo Deploying CloudFormation stack...
aws cloudformation deploy ^
    --template-file ../cloudformation.yml ^
    --stack-name %STACK_NAME% ^
    --parameter-overrides DockerImageUrl=%IMAGE_URL% ^
    --capabilities CAPABILITY_IAM ^
    --region %REGION%

if %ERRORLEVEL% neq 0 (
    echo Failed to deploy CloudFormation stack.
    exit /b 1
)

echo Getting service URL...
aws cloudformation describe-stacks ^
    --stack-name %STACK_NAME% ^
    --query "Stacks[0].Outputs[?OutputKey=='ServiceURL'].OutputValue" ^
    --output text ^
    --region %REGION%

echo Deployment completed successfully!
