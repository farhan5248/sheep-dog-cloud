# Kubernetes Deployment for Sheep Dog AWS

This directory contains Kubernetes manifests for deploying the Sheep Dog AWS application to a Kubernetes cluster.

## Directory Structure

- `base/`: Contains the base Kubernetes manifests
  - `deployment.yaml`: Defines the Kubernetes Deployment for the Sheep Dog AWS application
  - `service.yaml`: Defines the Kubernetes Service for exposing the application
  - `ingress.yaml`: Defines the Kubernetes Ingress for routing traffic to the service
  - `kustomization.yaml`: Kustomize configuration for the base resources

- `overlays/`: Contains environment-specific overlays
  - `dev/`: Development environment overlay
    - `kustomization.yaml`: Kustomize configuration for the dev environment
    - `ingress-patch.yaml`: Patch to set the host to aws.sheepdogdev.io

## Deployment Options

### Option 1: Deploy to AWS EKS using CloudFormation

Use the provided CloudFormation template and deployment script:

```bash
cd ../scripts
./deploy-to-eks.bat
```

This will:
1. Create an EKS cluster using CloudFormation
2. Configure kubectl to connect to the cluster
3. Deploy the application using the Kubernetes manifests

To tear down the EKS deployment:

```bash
cd ../scripts
./teardown-stack.bat eks
```

### Option 2: Deploy to an existing Kubernetes cluster (e.g., Minikube)

If you already have a Kubernetes cluster running (like Minikube), you can deploy directly using Kustomize:

```bash
# Make sure your kubectl is configured to use the correct cluster
kubectl apply -k overlays/dev/
```

Alternatively, you can apply the individual manifests:

```bash
kubectl apply -f base/deployment.yaml
kubectl apply -f base/service.yaml
kubectl apply -f base/ingress.yaml
```

## Accessing the Application

When deployed with a LoadBalancer service:
- For cloud providers (AWS, GCP, Azure), an external IP/hostname will be provisioned automatically
- For Minikube, you may need to use `minikube service sheep-dog-aws-service` to access the application

## Configuration

The deployment uses the following default configuration:
- Image: `ghcr.io/farhan5248/sheep-dog-aws:latest`
- Replicas: 2
- Container Port: 8080
- Service Port: 80

To modify these settings, edit the respective YAML files before deployment.
