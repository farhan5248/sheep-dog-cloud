#!/bin/bash

# Script to run the Spring Boot application locally

# Exit on error
set -e

# Default configuration
PORT=8080
PROFILE="dev"

# Parse command line arguments
while [[ $# -gt 0 ]]; do
  case $1 in
    --port)
      PORT="$2"
      shift 2
      ;;
    --profile)
      PROFILE="$2"
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

echo "Running application on port $PORT with profile $PROFILE..."
java -jar \
  -Dserver.port=$PORT \
  -Dspring.profiles.active=$PROFILE \
  target/aws-spring-boot-service-0.0.1-SNAPSHOT.jar
