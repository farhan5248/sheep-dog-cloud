@echo off
echo Tearing down AWS CloudFormation stack

REM Check if stack name is provided
if "%1"=="" (
    echo Usage: teardown-stack.bat [ecs^|eks]
    echo Example: teardown-stack.bat ecs
    exit /b 1
)

REM Set variables based on deployment type
if /i "%1"=="ecs" (
    set STACK_NAME=sheep-dog-aws-ecs
    echo Tearing down ECS deployment...
) else if /i "%1"=="eks" (
    set STACK_NAME=sheep-dog-aws-eks
    echo Tearing down EKS deployment...
) else (
    echo Invalid deployment type. Use 'ecs' or 'eks'.
    exit /b 1
)

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

REM For EKS, we need to clean up Kubernetes resources first
if /i "%1"=="eks" (
    echo Checking if kubectl is installed...
    kubectl version --client
    if %ERRORLEVEL% neq 0 (
        echo kubectl is not installed. Please install it first.
        exit /b 1
    )

    echo Getting EKS cluster name...
    for /f "tokens=*" %%i in ('aws cloudformation describe-stacks --stack-name %STACK_NAME% --query "Stacks[0].Outputs[?OutputKey=='ClusterName'].OutputValue" --output text --region %REGION%') do set CLUSTER_NAME=%%i

    if not "%CLUSTER_NAME%"=="" (
        echo Configuring kubectl to connect to the EKS cluster...
        aws eks update-kubeconfig --name %CLUSTER_NAME% --region %REGION%

        echo Deleting Kubernetes resources...
        kubectl delete -f ../kubernetes/service.yaml
        kubectl delete -f ../kubernetes/deployment.yaml
        
        echo Waiting for Kubernetes resources to be deleted...
        timeout /t 10
    )
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
