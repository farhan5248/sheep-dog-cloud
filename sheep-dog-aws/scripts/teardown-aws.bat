@echo off
echo Tearing down AWS ECS deployment

REM Set variables
set STACK_NAME=sheep-dog-aws
set REGION=us-east-1

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

echo Are you sure you want to delete the CloudFormation stack %STACK_NAME%? (y/n)
set /p CONFIRM=
if /i "%CONFIRM%" neq "y" (
    echo Operation cancelled.
    exit /b 0
)

echo Deleting CloudFormation stack...
aws cloudformation delete-stack --stack-name %STACK_NAME% --region %REGION%

echo Waiting for stack deletion to complete...
aws cloudformation wait stack-delete-complete --stack-name %STACK_NAME% --region %REGION%

if %ERRORLEVEL% neq 0 (
    echo Failed to delete CloudFormation stack.
    exit /b 1
)

echo CloudFormation stack deleted successfully!
