FROM ${docker.registry.github}/farhan5248/sheep-dog-dev-svc-dependencies:latest
COPY maven/target/dependency/META-INF /app/META-INF
COPY maven/target/dependency/BOOT-INF/classes /app/classes
ENTRYPOINT ["java","-cp","app/classes:app/lib/*","org.farhan.mbt.service.RestServiceApplication"]
