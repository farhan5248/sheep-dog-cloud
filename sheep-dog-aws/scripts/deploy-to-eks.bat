@echo off
echo Deploying Spring Boot service to AWS EKS

REM Set variables
set STACK_NAME=sheep-dog-aws-eks
set REGION=us-east-1
set IMAGE_URL=ghcr.io/farhan5248/sheep-dog-aws:latest

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
    --parameter-overrides DockerImageUrl=%IMAGE_URL% ^
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

echo Deploying application to EKS...
kubectl apply -f ../kubernetes/deployment.yaml
kubectl apply -f ../kubernetes/service.yaml

if %ERRORLEVEL% neq 0 (
    echo Failed to deploy application to EKS.
    exit /b 1
)

echo Waiting for service to get an external IP...
echo This may take a few minutes...
timeout /t 30

echo Getting service URL...
kubectl get service sheep-dog-aws-service -o jsonpath="{.status.loadBalancer.ingress[0].hostname}"

echo Deployment completed successfully!
