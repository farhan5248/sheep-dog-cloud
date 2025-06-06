# Sheep Dog AWS Spring Boot Service

This is a simple Spring Boot service with a "greeting" resource that can be deployed to AWS ECS as a Docker service.

## Project Structure

```
sheep-dog-aws/
├── src/                           # Source code
│   ├── main/
│   │   ├── java/                  # Java code
│   │   │   └── org/farhan/greeting/
│   │   │       ├── GreetingApplication.java
│   │   │       ├── controller/
│   │   │       │   └── GreetingController.java
│   │   │       └── model/
│   │   │           └── Greeting.java
│   │   └── resources/             # Application resources
│   │       └── application.properties
├── scripts/                       # Build and deployment scripts
│   ├── build-and-push.bat         # Script to build and push Docker image
│   ├── deploy-to-aws.bat          # Script to deploy to AWS
│   └── teardown-aws.bat           # Script to tear down AWS resources
├── Dockerfile                     # Docker configuration
├── pom.xml                        # Maven configuration
├── cloudformation.yml             # AWS CloudFormation template
└── README.md                      # This file
```

## Prerequisites

- Java 11 or later
- Maven
- Docker
- AWS CLI
- GitHub account with a Personal Access Token (PAT) with `write:packages` scope

## Local Development

### Building the Application

To build the Spring Boot application:

```bash
mvn clean package
```

### Running Locally

To run the application locally:

```bash
mvn spring-boot:run
```

The application will be available at http://localhost:8080/greeting

You can also pass a name parameter: http://localhost:8080/greeting?name=YourName

## Docker Image

### Building and Pushing the Docker Image

To build and push the Docker image to GitHub Container Registry:

1. Make sure you have Docker installed and running
2. Create a GitHub Personal Access Token with `write:packages` scope
3. Run the build-and-push script:

```bash
cd scripts
build-and-push.bat
```

When prompted, enter your GitHub Personal Access Token.

## AWS Deployment

### Setting Up AWS Permissions

Before deploying to AWS, you need to ensure your AWS user has the necessary permissions:

1. Make sure you have AWS CLI installed and configured
2. Run the setup-aws-permissions script:

```bash
cd scripts
setup-aws-permissions.bat
```

This script will:
- Check your current AWS permissions
- Provide guidance on how to set up the required permissions
- Test if you have the necessary permissions after setup

#### Permission Options

You have several options to set up the required permissions:

1. **Ask your AWS administrator** to grant you these permissions:
   - `cloudformation:*`
   - `iam:PassRole`
   - `ec2:*`
   - `elasticloadbalancing:*`
   - `ecs:*`
   - `ecr:*`
   - `logs:*`
   - `application-autoscaling:*`

2. **Set up permissions through the AWS Console**:
   1. Log in to the AWS Management Console
   2. Go to IAM service
   3. Select your user
   4. Click "Add permissions" and choose "Attach policies directly"
   5. Search for and attach these AWS managed policies:
      - AmazonECS-FullAccess
      - AmazonECR-FullAccess
      - AmazonEC2ContainerRegistryFullAccess
      - CloudWatchLogsFullAccess
      - AmazonVPCFullAccess
      - AWSCloudFormationFullAccess
      - IAMFullAccess (or a more restricted policy with PassRole permissions)

3. **Use AWS CloudShell** to deploy (if available in your region):
   1. Log in to the AWS Management Console
   2. Open CloudShell
   3. Upload your CloudFormation template and deployment scripts
   4. Run the deployment from CloudShell, which uses the console permissions

### Deploying to AWS

After setting up permissions, you can deploy the application to AWS ECS:

1. Run the deploy-to-aws script:

```bash
cd scripts
deploy-to-aws.bat
```

The script will:
- Deploy the CloudFormation stack
- Create all necessary AWS resources (VPC, ECS Cluster, Load Balancer, etc.)
- Deploy the Docker image to ECS
- Output the service URL

### Troubleshooting AWS Deployment

#### Permission Errors

If you encounter an error like:
```
An error occurred (AccessDenied) when calling the DescribeStacks operation: User: arn:aws:iam::XXXXXXXXXXXX:user/YourUsername is not authorized to perform: cloudformation:DescribeStacks
```

This indicates that your AWS user doesn't have the necessary permissions. Run the `setup-aws-permissions.bat` script and follow the instructions to set up the required permissions.

#### IAM Policy Creation Errors

If you see an error like:
```
An error occurred (AccessDenied) when calling the CreatePolicy operation: User: arn:aws:iam::XXXXXXXXXXXX:user/YourUsername is not authorized to perform: iam:CreatePolicy
```

This means you don't have permission to create IAM policies. Use one of the alternative methods described in the setup-aws-permissions.bat script:
1. Ask your AWS administrator to grant you the necessary permissions
2. Use the AWS Console to attach the required managed policies
3. Use AWS CloudShell to deploy with console permissions

#### Testing Your Permissions

After setting up permissions, you can test if you have the necessary permissions by running:
```bash
aws cloudformation list-stacks --max-items 1
aws ecs list-clusters --max-items 1
```

If these commands succeed, you should be able to deploy the CloudFormation stack.

### Tearing Down AWS Resources

To remove all AWS resources created by this deployment:

```bash
cd scripts
teardown-aws.bat
```

This will delete the CloudFormation stack and all associated resources.

## CloudFormation Template

The CloudFormation template (`cloudformation.yml`) creates the following resources:

- VPC with public subnets
- Internet Gateway and route tables
- Security Groups
- ECS Cluster
- ECS Task Definition
- ECS Service
- Application Load Balancer
- Auto Scaling configuration
- CloudWatch Logs Group
- IAM Roles

## API Endpoints

- `GET /greeting`: Returns a greeting message
  - Query Parameters:
    - `name` (optional): The name to greet (default: "World")
  - Response: JSON object with `id` and `content` fields

## GitHub Container Registry

The Docker image is stored in GitHub Container Registry at:
`ghcr.io/farhan5248/sheep-dog-aws:latest`

## Notes

- The application uses Spring Boot Actuator for health checks
- The ECS service is configured to auto-scale based on CPU and memory utilization
- The CloudFormation template uses sensible defaults but can be customized as needed
