#!/bin/bash

# Script to run the Spring Boot application using Docker Compose

# Exit on error
set -e

# Default configuration
ACTION="up"

# Parse command line arguments
while [[ $# -gt 0 ]]; do
  case $1 in
    --action)
      ACTION="$2"
      shift 2
      ;;
    *)
      echo "Unknown option: $1"
      exit 1
      ;;
  esac
done

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
  echo "Docker is not running. Please start Docker and try again."
  exit 1
fi

# Build the application
if [ "$ACTION" = "up" ] || [ "$ACTION" = "build" ]; then
  echo "Building application..."
  mvn clean package -DskipTests
fi

# Perform the requested Docker Compose action
case $ACTION in
  up)
    echo "Starting containers..."
    docker-compose up -d
    echo "Application is running at http://localhost:8080"
    ;;
  down)
    echo "Stopping containers..."
    docker-compose down
    ;;
  build)
    echo "Building Docker image..."
    docker-compose build
    ;;
  logs)
    echo "Showing logs..."
    docker-compose logs -f
    ;;
  restart)
    echo "Restarting containers..."
    docker-compose restart
    ;;
  *)
    echo "Unknown action: $ACTION"
    echo "Supported actions: up, down, build, logs, restart"
    exit 1
    ;;
esac

echo "Done!"
