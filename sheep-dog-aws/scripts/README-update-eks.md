# EKS Cluster Update Script

This script (`update-eks.bat`) is designed to update an existing EKS cluster with new Docker images or Kubernetes configuration changes. It assumes that the initial deployment has already been performed using the `deploy-to-eks.bat` script.

## Prerequisites

- AWS CLI installed and configured
- kubectl installed
- Docker installed (if building and pushing images)
- Maven installed (if building the application)
- An existing EKS cluster deployed using the `deploy-to-eks.bat` script

## Usage

```bash
cd scripts
./update-eks.bat [options]
```

### Options

- `--build-image`: Build and push the Docker image before updating the EKS cluster
- `--no-config`: Skip applying Kubernetes configuration changes (useful if you only want to update the Docker image)
- `--help`: Display help message

## Examples

### Update Kubernetes Configuration Only

```bash
./update-eks.bat
```

This will apply any changes to the Kubernetes configuration files and restart the deployment to ensure the changes take effect.

### Build and Push Docker Image, Then Update

```bash
./update-eks.bat --build-image
```

This will:
1. Build the Spring Boot application with Maven
2. Build the Docker images (dependencies and application)
3. Push the Docker images to GitHub Container Registry
4. Apply Kubernetes configuration changes
5. Restart the deployment to pull the latest image

### Update Docker Image Without Changing Configuration

```bash
./update-eks.bat --build-image --no-config
```

This will build and push the Docker image, then restart the deployment to pull the latest image, without applying any Kubernetes configuration changes.

## How It Works

1. The script checks for required tools (AWS CLI, kubectl, kustomize)
2. Verifies AWS login
3. Gets the EKS cluster name from the CloudFormation stack
4. Configures kubectl to connect to the EKS cluster
5. Optionally builds and pushes the Docker image
6. Optionally applies Kubernetes configuration changes
7. Restarts the deployment to pull the latest image
8. Waits for the rollout to complete
9. Displays the service and ingress URLs

## Troubleshooting

If the script fails, check the error message for details. Common issues include:

- AWS CLI not installed or not configured correctly
- kubectl not installed or not configured correctly
- Not logged in to AWS
- CloudFormation stack not found (make sure you've run `deploy-to-eks.bat` first)
- Docker not installed or not configured correctly
- Not logged in to GitHub Container Registry
- Kubernetes configuration errors

## Notes

- The script uses the `latest` tag for Docker images. If you need to use a specific version, modify the `VERSION` variable in the script.
- The script assumes the deployment is in the `dev` namespace. If you're using a different namespace, modify the script accordingly.
- The script assumes the deployment name is `sheep-dog-aws`. If you're using a different name, modify the script accordingly.
