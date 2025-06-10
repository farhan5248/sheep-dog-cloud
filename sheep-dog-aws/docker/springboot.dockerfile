FROM ghcr.io/farhan5248/sheep-dog-aws-dependencies:latest

# Copy only application code and metadata
COPY maven/target/dependency/META-INF /app/META-INF
COPY maven/target/dependency/BOOT-INF/classes /app/classes

EXPOSE 8080

# Set environment variables for JMS
ENV SPRING_ARTEMIS_HOST=${ARTEMIS_HOST}
ENV SPRING_ARTEMIS_PORT=${ARTEMIS_PORT}
ENV SPRING_ARTEMIS_USER=${ARTEMIS_USER}
ENV SPRING_ARTEMIS_PASSWORD=${ARTEMIS_PASSWORD}

ENTRYPOINT ["java", "-cp", "app/classes:app/lib/*", "org.farhan.greeting.GreetingApplication"]
