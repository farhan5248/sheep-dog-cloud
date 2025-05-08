FROM eclipse-temurin:21-jdk-alpine
LABEL "maintainer"=farhan.sheikh.5248@gmail.com
RUN apk --no-cache add curl
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
# Extract layers from jar for better caching
COPY maven/target/dependency/BOOT-INF/lib /app/lib
COPY maven/target/dependency/META-INF /app/META-INF
COPY maven/target/dependency/BOOT-INF/classes /app/classes
ENTRYPOINT ["java","-cp","app/classes:app/lib/*","org.farhan.mbt.service.RestServiceApplication"]