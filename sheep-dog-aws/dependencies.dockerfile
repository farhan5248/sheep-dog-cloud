FROM openjdk:11-jre-slim

# Extract and copy only the dependencies
COPY target/extracted-dependencies/BOOT-INF/lib /app/lib
