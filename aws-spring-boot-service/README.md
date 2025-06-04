# AWS Spring Boot Service

A Spring Boot application with AWS integration for deployment to AWS.

## Features

- RESTful API with Spring Boot
- Database integration with Spring Data JPA
- AWS S3 integration for file storage
- Unit tests with JUnit and Mockito

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- AWS account with appropriate permissions
- AWS CLI configured with your credentials

## Project Structure

```
aws-spring-boot-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/farhan/aws/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       └── AwsSpringBootServiceApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── org/farhan/aws/
│               └── controller/
└── pom.xml
```

## Building the Application

To build the application, run:

```bash
mvn clean package
```

This will create a JAR file in the `target` directory.

## Running the Application Locally

### Using Maven

To run the application locally, use:

```bash
mvn spring-boot:run
```

Or run the JAR file directly:

```bash
java -jar target/aws-spring-boot-service-0.0.1-SNAPSHOT.jar
```

### Using the provided scripts

#### On Linux/Mac:
```bash
# Make the scripts executable
chmod +x run-local.sh run-docker.sh deploy.sh

# Run locally
./run-local.sh

# Run with Docker
./run-docker.sh

# Deploy to AWS
./deploy.sh
```

#### On Windows:
```cmd
# Run locally
run-local.bat

# Run with Docker
run-docker.bat

# Deploy to AWS
deploy.bat
```

You can pass additional parameters to the scripts:

```bash
# Run on a different port
./run-local.sh --port 9090

# Run with a different profile
./run-local.sh --profile prod

# Docker operations
./run-docker.sh --action up    # Start containers (default)
./run-docker.sh --action down  # Stop containers
./run-docker.sh --action logs  # View logs
./run-docker.sh --action build # Build image
```

The application will start on port 8080 by default.

## API Endpoints

### Product API

- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get a product by ID
- `POST /api/products` - Create a new product
- `PUT /api/products/{id}` - Update a product
- `DELETE /api/products/{id}` - Delete a product

### S3 API

- `GET /api/s3/buckets` - List all S3 buckets
- `GET /api/s3/buckets/{bucketName}/objects` - List objects in a bucket
- `POST /api/s3/buckets/{bucketName}/objects/{objectKey}` - Upload an object
- `GET /api/s3/buckets/{bucketName}/objects/{objectKey}` - Download an object
- `DELETE /api/s3/buckets/{bucketName}/objects/{objectKey}` - Delete an object

## Deploying to AWS

### Prerequisites

- AWS CLI installed and configured
- AWS account with appropriate permissions

### Deployment Options

#### 1. Using the provided deployment scripts

The easiest way to deploy is using the provided deployment scripts:

```bash
# On Linux/Mac
./deploy.sh

# On Windows
deploy.bat
```

You can customize the deployment with parameters:

```bash
./deploy.sh --stack-name my-custom-stack --region us-west-2 --env prod
```

#### 2. Using AWS CloudFormation directly

You can also deploy using the AWS CloudFormation template directly:

```bash
# Build the application
mvn clean package

# Create an S3 bucket for deployment (if it doesn't exist)
aws s3api create-bucket --bucket my-deployment-bucket --region us-east-1

# Upload the JAR file to S3
aws s3 cp target/aws-spring-boot-service-0.0.1-SNAPSHOT.jar s3://my-deployment-bucket/

# Deploy the CloudFormation stack
aws cloudformation deploy \
  --template-file cloudformation.yml \
  --stack-name aws-spring-boot-service \
  --parameter-overrides \
    EnvironmentName=dev \
    S3BucketName=my-deployment-bucket \
  --capabilities CAPABILITY_IAM \
  --region us-east-1
```

#### 3. Using AWS Elastic Beanstalk

You can also deploy to AWS Elastic Beanstalk:

```bash
# Install the EB CLI
pip install awsebcli

# Initialize EB CLI
eb init

# Create an environment
eb create

# Deploy the application
eb deploy
```

#### 4. Manual deployment to EC2

1. Create an EC2 instance
2. Install Java on the instance
3. Copy the JAR file to the instance
4. Run the JAR file on the instance

## Docker Support

This project includes Docker support for containerized deployment and development.

### Running with Docker

You can run the application using Docker with the provided scripts:

```bash
# On Linux/Mac
./run-docker.sh

# On Windows
run-docker.bat
```

Or manually with Docker commands:

```bash
# Build the Docker image
docker build -t aws-spring-boot-service .

# Run the container
docker run -p 8080:8080 aws-spring-boot-service
```

### Docker Compose

The project includes a `docker-compose.yml` file for easy local development:

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

The Docker Compose file includes:
- The Spring Boot application
- Commented out configurations for MySQL database
- Commented out configurations for LocalStack (AWS services simulator)

You can uncomment these sections in the `docker-compose.yml` file to enable them.

## Configuration

The application can be configured using the following properties in `application.properties`:

- `server.port` - The port the server will run on
- `spring.datasource.*` - Database configuration
- `aws.region` - AWS region to use

You can also use environment variables to configure the application when running with Docker:

```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e AWS_REGION=us-west-2 \
  aws-spring-boot-service
```

## Testing

To run the tests, use:

```bash
mvn test
```

## License

This project is licensed under the MIT License.
