FROM eclipse-temurin:21-jre

# Install additional dependencies
RUN apt-get update && apt-get install -y --no-install-recommends \
    libc6 \
    libssl1.1 \
    && rm -rf /var/lib/apt/lists/*

# Set environment variables for JMS
ENV ARTEMIS_HOST=sheep-dog-aws-artemis
ENV ARTEMIS_PORT=61616
ENV ARTEMIS_USER=artemis
ENV ARTEMIS_PASSWORD=artemis

# Extract and copy only the dependencies
COPY target/extracted-dependencies/BOOT-INF/lib /app/lib
