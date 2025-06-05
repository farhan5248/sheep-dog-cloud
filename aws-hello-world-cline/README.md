# AWS Hello World Spring Boot Service

This is a simple Spring Boot service with a "greeting" resource that can be deployed to AWS ECS as a Docker service.

## Project Structure

```
aws-hello-world-cline/
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

### Deploying to AWS

To deploy the application to AWS ECS:

1. Make sure you have AWS CLI installed and configured
2. Run the deploy-to-aws script:

```bash
cd scripts
deploy-to-aws.bat
```

The script will:
- Deploy the CloudFormation stack
- Create all necessary AWS resources (VPC, ECS Cluster, Load Balancer, etc.)
- Deploy the Docker image to ECS
- Output the service URL

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
`ghcr.io/farhan5248/aws-hello-world-cline:latest`

## Notes

- The application uses Spring Boot Actuator for health checks
- The ECS service is configured to auto-scale based on CPU and memory utilization
- The CloudFormation template uses sensible defaults but can be customized as needed
