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

To update the EKS deployment with new Docker images or Kubernetes configuration changes:

```bash
cd scripts
./update-eks.bat [options]
```

See `scripts/README-update-eks.md` for detailed usage instructions and options.

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
- `scripts/update-eks.bat`: Script to update an existing EKS deployment with new Docker images or Kubernetes configuration changes
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

## Troubleshooting

### VPC and Load Balancer Deletion Issues

If you encounter issues where CloudFormation cannot delete the VPC when tearing down a stack, it's likely due to missing specific permissions for VPC resources and/or Elastic Load Balancers. The CloudFormation stack requires explicit permissions to delete these resources and their dependencies.

#### Common Issue: Load Balancer Preventing VPC Deletion

A common issue is that the Elastic Load Balancer (ELB) cannot be deleted due to missing permissions, which then prevents the VPC from being deleted. This is particularly common with the ECS deployment which uses an Application Load Balancer.

To fix these issues:

1. Ensure your IAM policy includes the specific VPC deletion permissions listed in `scripts/cloudformation-policy.json`:
   - `ec2:DeleteVpc`
   - `ec2:DeleteSubnet`
   - `ec2:DeleteRouteTable`
   - `ec2:DeleteNetworkAcl`
   - `ec2:DeleteSecurityGroup`
   - `ec2:DeleteInternetGateway`
   - `ec2:DetachInternetGateway`
   - And other network interface and VPC endpoint related permissions

2. Ensure your IAM policy includes the specific Load Balancer deletion permissions:
   - `elasticloadbalancing:DeleteLoadBalancer`
   - `elasticloadbalancing:DeleteTargetGroup`
   - `elasticloadbalancing:DeleteListener`
   - `elasticloadbalancing:DeregisterTargets`
   - And other load balancer related permissions

The updated policy in `scripts/cloudformation-policy.json` includes all necessary permissions to properly delete both VPC and Load Balancer resources when tearing down the CloudFormation stack.

#### Implemented Solutions

To prevent VPC and load balancer deletion issues, we've implemented two key approaches:

1. **Improved CloudFormation Deletion Order (EKS)**:
   - Added `DependsOn` attributes to ensure resources are deleted in the correct order
   - The VPC now depends on the EKS Node Group and Cluster, ensuring it's deleted last
   - Added explicit `DeletionPolicy` and `UpdateReplacePolicy` attributes to critical resources

2. **Pre-deletion Hook for Load Balancers (EKS)**:
   - The `teardown-stack.bat` script now explicitly deletes Kubernetes LoadBalancer services before deleting the CloudFormation stack
   - Includes a wait period to ensure load balancers are fully deleted
   - Checks for lingering load balancers before proceeding with stack deletion

#### Troubleshooting Steps

If you still encounter issues:

1. Check if the load balancer is still present:
   ```bash
   aws elbv2 describe-load-balancers --query "LoadBalancers[?contains(LoadBalancerName, 'sheep-dog-aws')].LoadBalancerName" --output text
   ```

2. If found, try manually deleting it:
   ```bash
   aws elbv2 delete-load-balancer --load-balancer-arn YOUR_LOAD_BALANCER_ARN
   ```

3. For EKS deployments, manually delete Kubernetes services of type LoadBalancer:
   ```bash
   kubectl get svc --all-namespaces | grep LoadBalancer
   kubectl delete svc SERVICE_NAME -n NAMESPACE
   ```

4. Wait a few minutes for AWS to clean up the load balancer resources, then try deleting the CloudFormation stack again.
