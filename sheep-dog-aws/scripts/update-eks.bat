@echo off
echo Updating EKS cluster with new Docker image or Kubernetes configuration changes

REM Set variables
set STACK_NAME=sheep-dog-aws-eks
set REGION=us-east-1
set BUILD_IMAGE=false
set APPLY_CONFIG=true

REM Parse command line arguments
:parse_args
if "%~1"=="" goto :end_parse_args
if /i "%~1"=="--build-image" (
    set BUILD_IMAGE=true
    shift
    goto :parse_args
)
if /i "%~1"=="--no-config" (
    set APPLY_CONFIG=false
    shift
    goto :parse_args
)
if /i "%~1"=="--help" (
    echo Usage: update-eks.bat [options]
    echo Options:
    echo   --build-image    Build and push Docker image before updating
    echo   --no-config      Skip applying Kubernetes configuration changes
    echo   --help           Display this help message
    exit /b 0
)
shift
goto :parse_args
:end_parse_args

echo Checking if AWS CLI is installed...
aws --version
if %ERRORLEVEL% neq 0 (
    echo AWS CLI is not installed. Please install it first.
    exit /b 1
)

echo Checking if kubectl is installed...
kubectl version --client
if %ERRORLEVEL% neq 0 (
    echo kubectl is not installed. Please install it first.
    exit /b 1
)

echo Checking if kustomize is installed...
kubectl kustomize --help > nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo Kustomize functionality is not available. It should be included with kubectl v1.14+.
    echo If you're using an older version, please install kustomize separately.
    exit /b 1
)

echo Checking if you are logged in to AWS...
aws sts get-caller-identity
if %ERRORLEVEL% neq 0 (
    echo You are not logged in to AWS. Please run 'aws configure' first.
    exit /b 1
)

echo Getting EKS cluster name from CloudFormation stack...
for /f "tokens=*" %%i in ('aws cloudformation describe-stacks --stack-name %STACK_NAME% --query "Stacks[0].Outputs[?OutputKey=='ClusterName'].OutputValue" --output text --region %REGION%') do set CLUSTER_NAME=%%i

if "%CLUSTER_NAME%"=="" (
    echo Failed to get EKS cluster name from CloudFormation stack.
    echo Make sure the stack %STACK_NAME% exists and has been deployed using deploy-to-eks.bat.
    exit /b 1
)

echo Configuring kubectl to connect to the EKS cluster %CLUSTER_NAME%...
aws eks update-kubeconfig --name %CLUSTER_NAME% --region %REGION%

if %ERRORLEVEL% neq 0 (
    echo Failed to configure kubectl.
    exit /b 1
)

if "%BUILD_IMAGE%"=="true" (
    echo Building and pushing Docker image...
    cd ..
    
    echo Building Spring Boot application with Maven...
    call mvn clean package -DskipTests
    
    echo Building and pushing Docker images...
    set DEPENDENCIES_IMAGE_NAME=ghcr.io/farhan5248/sheep-dog-aws-dependencies
    set APP_IMAGE_NAME=ghcr.io/farhan5248/sheep-dog-aws
    set VERSION=latest
    
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
    
    cd scripts
)

if "%APPLY_CONFIG%"=="true" (
    echo Updating Kubernetes configuration...
    echo Creating namespace if it doesn't exist...
    kubectl create namespace dev --dry-run=client -o yaml | kubectl apply -f -
    
    echo Applying Kustomize overlay...
    cd ..
    kubectl apply -k kubernetes/overlays/dev/
    
    if %ERRORLEVEL% neq 0 (
        echo Failed to apply Kubernetes configuration.
        exit /b 1
    )
    cd scripts
)

echo Restarting deployment to pull the latest image...
kubectl rollout restart deployment sheep-dog-aws -n dev

echo Waiting for rollout to complete...
kubectl rollout status deployment sheep-dog-aws -n dev

if %ERRORLEVEL% neq 0 (
    echo Deployment rollout failed.
    exit /b 1
)

echo Getting service URL...
kubectl get service -n dev sheep-dog-aws-service -o jsonpath="{.status.loadBalancer.ingress[0].hostname}"
echo.

echo Getting Ingress URL...
kubectl get ingress -n dev sheep-dog-aws-ingress -o jsonpath="{.spec.rules[0].host}"
echo.

echo EKS cluster update completed successfully!
