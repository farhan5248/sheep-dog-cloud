FROM eclipse-temurin:21-jdk-alpine
LABEL "maintainer"=farhan.sheikh.5248@gmail.com
RUN apk --no-cache add curl
COPY maven/target/dependency/BOOT-INF/lib /app/lib