@echo off
REM Script to build and deploy the Spring Boot application to AWS on Windows

REM Default configuration
set STACK_NAME=aws-spring-boot-service
set S3_BUCKET=aws-spring-boot-service-deployment
set REGION=us-east-1
set ENV=dev

REM Parse command line arguments
:parse_args
if "%~1"=="" goto :end_parse_args
if "%~1"=="--stack-name" (
    set STACK_NAME=%~2
    shift
    shift
    goto :parse_args
)
if "%~1"=="--s3-bucket" (
    set S3_BUCKET=%~2
    shift
    shift
    goto :parse_args
)
if "%~1"=="--region" (
    set REGION=%~2
    shift
    shift
    goto :parse_args
)
if "%~1"=="--env" (
    set ENV=%~2
    shift
    shift
    goto :parse_args
)
echo Unknown option: %~1
exit /b 1
:end_parse_args

echo Building application...
call mvn clean package -DskipTests

echo Creating S3 bucket for deployment if it doesn't exist...
aws s3api create-bucket --bucket %S3_BUCKET% --region %REGION% || echo Bucket may already exist, continuing...

echo Uploading application JAR to S3...
aws s3 cp target\aws-spring-boot-service-0.0.1-SNAPSHOT.jar s3://%S3_BUCKET%/

echo Deploying CloudFormation stack...
aws cloudformation deploy ^
  --template-file cloudformation.yml ^
  --stack-name %STACK_NAME% ^
  --parameter-overrides ^
    EnvironmentName=%ENV% ^
    S3BucketName=%S3_BUCKET% ^
  --capabilities CAPABILITY_IAM ^
  --region %REGION%

echo Deployment completed successfully!
echo Getting stack outputs...
aws cloudformation describe-stacks ^
  --stack-name %STACK_NAME% ^
  --region %REGION% ^
  --query "Stacks[0].Outputs" ^
  --output table
