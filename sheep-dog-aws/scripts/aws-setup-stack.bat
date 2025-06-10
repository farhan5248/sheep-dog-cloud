@echo off
echo Deploying Spring Boot service to AWS EKS

REM Check if suffix is provided
set SUFFIX=%1
set BASE_STACK_NAME=sheep-dog-aws-eks

REM Set variables
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

echo Deploying CloudFormation stack for EKS...
aws cloudformation deploy ^
    --template-file ../cloudformation-eks.yml ^
    --stack-name %STACK_NAME% ^
    --capabilities CAPABILITY_IAM ^
    --region %REGION%

if %ERRORLEVEL% neq 0 (
    echo Failed to deploy CloudFormation stack.
    exit /b 1
)

echo Getting EKS cluster name...
for /f "tokens=*" %%i in ('aws cloudformation describe-stacks --stack-name %STACK_NAME% --query "Stacks[0].Outputs[?OutputKey=='ClusterName'].OutputValue" --output text --region %REGION%') do set CLUSTER_NAME=%%i

echo Configuring kubectl to connect to the EKS cluster...
aws eks update-kubeconfig --name %CLUSTER_NAME% --region %REGION%

if %ERRORLEVEL% neq 0 (
    echo Failed to configure kubectl.
    exit /b 1
)

echo Deploying application to EKS using Kustomize...
echo Creating namespace if it doesn't exist...
kubectl create namespace dev --dry-run=client -o yaml | kubectl apply -f -

echo Applying Kustomize overlay...
kubectl apply -k ../kubernetes/overlays/dev/

if %ERRORLEVEL% neq 0 (
    echo Failed to deploy application to EKS.
    exit /b 1
)

echo Waiting for service to get an external IP...
echo This may take a few minutes...
timeout /t 30

echo Getting service URL...
kubectl get service -n dev sheep-dog-aws-service -o jsonpath="{.status.loadBalancer.ingress[0].hostname}"

echo Getting Ingress URL...
kubectl get ingress -n dev sheep-dog-aws-ingress -o jsonpath="{.spec.rules[0].host}"

echo Deployment completed successfully!
