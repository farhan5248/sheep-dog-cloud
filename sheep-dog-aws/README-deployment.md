# Sheep Dog AWS Deployment Options

This project supports multiple deployment options for the Sheep Dog AWS application:

## 1. AWS ECS Deployment

Deploy the application to AWS Elastic Container Service (ECS) using Fargate:

```bash
cd scripts
./deploy-to-ecs.bat
```

This will:
- Create a CloudFormation stack with all necessary resources
- Deploy the Docker container to ECS Fargate
- Set up auto-scaling, load balancing, and networking

To tear down the ECS deployment:

```bash
cd scripts
./teardown-stack.bat ecs
```

## 2. AWS EKS Deployment

Deploy the application to AWS Elastic Kubernetes Service (EKS):

```bash
cd scripts
./deploy-to-eks.bat
```

This will:
- Create an EKS cluster using CloudFormation
- Configure kubectl to connect to the cluster
- Deploy the application using Kubernetes manifests

To tear down the EKS deployment:

```bash
cd scripts
./teardown-stack.bat eks
```

## 3. Local Kubernetes Deployment (e.g., Minikube)

If you already have a Kubernetes cluster running locally (like Minikube), you can deploy directly using Kustomize:

```bash
cd kubernetes
# Make sure your kubectl is configured to use the correct cluster
kubectl apply -k overlays/dev/
```

This will deploy the application with the host configured as `aws.sheepdogdev.io` in the Ingress resource.

## Deployment Files

- `cloudformation-ecs.yml`: CloudFormation template for ECS deployment
- `cloudformation-eks.yml`: CloudFormation template for EKS deployment
- `kubernetes/`: Directory containing Kubernetes manifests
  - `base/`: Base Kubernetes manifests
    - `deployment.yaml`: Kubernetes Deployment configuration
    - `service.yaml`: Kubernetes Service configuration
    - `ingress.yaml`: Kubernetes Ingress configuration
    - `kustomization.yaml`: Kustomize configuration for base resources
  - `overlays/`: Environment-specific overlays
    - `dev/`: Development environment overlay
      - `kustomization.yaml`: Kustomize configuration for dev environment
      - `ingress-patch.yaml`: Patch to set the host to aws.sheepdogdev.io
  - `README.md`: Detailed instructions for Kubernetes deployment

## Deployment Scripts

- `scripts/deploy-to-ecs.bat`: Script to deploy to AWS ECS
- `scripts/deploy-to-eks.bat`: Script to deploy to AWS EKS
- `scripts/teardown-stack.bat`: Unified script to tear down either ECS or EKS deployments

## Configuration

The default configuration uses:
- Image: `ghcr.io/farhan5248/sheep-dog-aws:latest`
- Container Port: 8080
- Desired Count: 2 instances
- Max Count: 4 instances

To modify these settings, edit the respective CloudFormation templates or Kubernetes manifests before deployment.

## AWS Permissions

Before deploying to AWS, ensure you have the necessary permissions:

```bash
cd scripts
./setup-aws-permissions.bat
```

This script will guide you through setting up the required permissions for both ECS and EKS deployments. The main permissions needed are:

- CloudFormation permissions
- IAM permissions (for creating roles)
- EC2 and VPC permissions
- ECS permissions (for ECS deployment)
- EKS permissions (for EKS deployment)
- ECR permissions (for container registry)
- CloudWatch Logs permissions
