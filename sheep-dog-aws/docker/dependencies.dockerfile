FROM eclipse-temurin:21-jre

# Set environment variables for JMS
ENV ARTEMIS_HOST=sheep-dog-aws-artemis
ENV ARTEMIS_PORT=61616
ENV ARTEMIS_USER=artemis
ENV ARTEMIS_PASSWORD=artemis

# Extract and copy only the dependencies
COPY maven/target/dependency/BOOT-INF/lib /app/lib
