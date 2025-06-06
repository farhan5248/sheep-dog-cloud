# Kubernetes Deployment for Sheep Dog AWS

This directory contains Kubernetes manifests for deploying the Sheep Dog AWS application to a Kubernetes cluster.

## Files

- `deployment.yaml`: Defines the Kubernetes Deployment for the Sheep Dog AWS application
- `service.yaml`: Defines the Kubernetes Service for exposing the application using a LoadBalancer

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

If you already have a Kubernetes cluster running (like Minikube), you can deploy directly:

```bash
# Make sure your kubectl is configured to use the correct cluster
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
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
