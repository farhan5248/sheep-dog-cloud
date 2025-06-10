@echo off
echo Tearing down AWS CloudFormation stack

REM Check if deployment type is provided
if "%1"=="" (
    echo Usage: teardown-stack.bat [suffix]
    echo Example with suffix: teardown-stack.bat 1
    exit /b 1
) else (
    set SUFFIX=%1
    set BASE_STACK_NAME=sheep-dog-aws-eks
    set STACK_NAME=%BASE_STACK_NAME%-%SUFFIX%
    set REGION=us-east-1
    echo Using stack name with suffix: %STACK_NAME%
)

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

    echo Deleting all LoadBalancer services to ensure proper cleanup...
    kubectl get svc --all-namespaces -o json > services.json
    for /f "tokens=*" %%i in ('type services.json ^| findstr "LoadBalancer"') do (
        echo Found LoadBalancer service: %%i
        REM Extract namespace and service name and delete it
        kubectl get svc --all-namespaces | findstr "LoadBalancer" | for /f "tokens=1,2" %%a in ('more') do (
            echo Deleting service %%b in namespace %%a
            kubectl delete svc %%b -n %%a
        )
    )
    del services.json

    echo Waiting for load balancers to be fully deleted...
    timeout /t 60

    echo Checking for lingering load balancers...
    aws elbv2 describe-load-balancers --query "LoadBalancers[?contains(LoadBalancerName, '%STACK_NAME%')].LoadBalancerName" --output text
    if not "%ERRORLEVEL%"=="0" (
        echo WARNING: Found lingering load balancers that may prevent VPC deletion.
        echo You may need to delete these manually before proceeding.
        echo Continue anyway? (y/n)
        set /p CONTINUE=
        if /i "%CONTINUE%" neq "y" (
            echo Operation cancelled.
            exit /b 0
        )
    )

    echo Deleting Kubernetes resources...
    kubectl delete -k ../kubernetes/overlays/dev/
    
    echo Waiting for Kubernetes resources to be deleted...
    timeout /t 30
)

echo Deleting CloudFormation stack...
aws cloudformation delete-stack --stack-name %STACK_NAME% --region %REGION%

echo Waiting for stack deletion to complete...
aws cloudformation wait stack-delete-complete --stack-name %STACK_NAME% --region %REGION%

if %ERRORLEVEL% neq 0 (
    echo Failed to delete CloudFormation stack.
    exit /b 1
) else (
    echo CloudFormation stack deleted successfully!
)