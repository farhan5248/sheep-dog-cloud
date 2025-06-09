@echo off
echo Tearing down AWS CloudFormation stack

REM Check if deployment type is provided
if "%1"=="" (
    echo Usage: teardown-stack.bat [ecs^|eks] [suffix]
    echo Example: teardown-stack.bat ecs
    echo Example with suffix: teardown-stack.bat eks 1
    exit /b 1
)

REM Get suffix if provided
set SUFFIX=%2

REM Set variables based on deployment type
if /i "%1"=="ecs" (
    set BASE_STACK_NAME=sheep-dog-aws-ecs
    echo Tearing down ECS deployment...
) else if /i "%1"=="eks" (
    set BASE_STACK_NAME=sheep-dog-aws-eks
    echo Tearing down EKS deployment...
) else (
    echo Invalid deployment type. Use 'ecs' or 'eks'.
    exit /b 1
)

REM Apply suffix if provided
if "%SUFFIX%"=="" (
    set STACK_NAME=%BASE_STACK_NAME%
    echo Using default stack name: %STACK_NAME%
) else (
    set STACK_NAME=%BASE_STACK_NAME%-%SUFFIX%
    echo Using stack name with suffix: %STACK_NAME%
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
        kubectl delete -k ../kubernetes/overlays/dev/
        
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
    
    echo.
    echo ========================================================================
    echo TROUBLESHOOTING VPC AND LOAD BALANCER DELETION ISSUES
    echo ========================================================================
    echo.
    echo If the stack deletion failed, it's often due to load balancer or VPC deletion issues:
    echo.
    echo 1. Check for load balancers that might be preventing deletion:
    echo    - Run: aws elbv2 describe-load-balancers --query "LoadBalancers[?contains(LoadBalancerName, '%STACK_NAME%')].LoadBalancerName" --output text
    echo    - If found, get the ARN: aws elbv2 describe-load-balancers --names LOAD_BALANCER_NAME --query "LoadBalancers[0].LoadBalancerArn" --output text
    echo    - Delete it manually: aws elbv2 delete-load-balancer --load-balancer-arn LOAD_BALANCER_ARN
    echo.
    echo 2. Check for target groups:
    echo    - Run: aws elbv2 describe-target-groups --query "TargetGroups[?contains(TargetGroupName, '%STACK_NAME%')].TargetGroupName" --output text
    echo    - Delete them: aws elbv2 delete-target-group --target-group-arn TARGET_GROUP_ARN
    echo.
    echo 3. Ensure you have the necessary permissions:
    echo    - Check if your IAM policy includes specific VPC and Load Balancer deletion permissions
    echo    - See scripts/cloudformation-policy.json for the required permissions
    echo.
    echo 4. Check for resources that might be preventing VPC deletion:
    echo    - Run: aws ec2 describe-network-interfaces --filters Name=vpc-id,Values=YOUR_VPC_ID
    echo    - Look for any network interfaces not managed by CloudFormation
    echo.
    echo 5. Check for VPC endpoints:
    echo    - Run: aws ec2 describe-vpc-endpoints --filters Name=vpc-id,Values=YOUR_VPC_ID
    echo.
    echo 6. For more information, see the Troubleshooting section in README-deployment.md
    echo.
    
    exit /b 1
)

echo CloudFormation stack deleted successfully!
