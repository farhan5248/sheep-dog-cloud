FROM ghcr.io/farhan5248/sheep-dog-aws-dependencies:latest

WORKDIR /app

# Copy only application code and metadata
COPY target/extracted-dependencies/META-INF /app/META-INF
COPY target/extracted-dependencies/BOOT-INF/classes /app/classes

EXPOSE 8080

ENTRYPOINT ["java", "-cp", "app/classes:app/lib/*", "org.farhan.greeting.GreetingApplication"]
