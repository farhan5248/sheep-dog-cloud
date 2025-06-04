#!/bin/bash

# Script to build and deploy the Spring Boot application to AWS

# Exit on error
set -e

# Configuration
STACK_NAME="aws-spring-boot-service"
S3_BUCKET="aws-spring-boot-service-deployment"
REGION="us-east-1"
ENV="dev"

# Parse command line arguments
while [[ $# -gt 0 ]]; do
  case $1 in
    --stack-name)
      STACK_NAME="$2"
      shift 2
      ;;
    --s3-bucket)
      S3_BUCKET="$2"
      shift 2
      ;;
    --region)
      REGION="$2"
      shift 2
      ;;
    --env)
      ENV="$2"
      shift 2
      ;;
    *)
      echo "Unknown option: $1"
      exit 1
      ;;
  esac
done

echo "Building application..."
mvn clean package -DskipTests

echo "Creating S3 bucket for deployment if it doesn't exist..."
aws s3api create-bucket --bucket $S3_BUCKET --region $REGION || true

echo "Uploading application JAR to S3..."
aws s3 cp target/aws-spring-boot-service-0.0.1-SNAPSHOT.jar s3://$S3_BUCKET/

echo "Deploying CloudFormation stack..."
aws cloudformation deploy \
  --template-file cloudformation.yml \
  --stack-name $STACK_NAME \
  --parameter-overrides \
    EnvironmentName=$ENV \
    S3BucketName=$S3_BUCKET \
  --capabilities CAPABILITY_IAM \
  --region $REGION

echo "Deployment completed successfully!"
echo "Getting stack outputs..."
aws cloudformation describe-stacks \
  --stack-name $STACK_NAME \
  --region $REGION \
  --query "Stacks[0].Outputs" \
  --output table
