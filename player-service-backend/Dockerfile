# Dockerfile for Spring Boot Backend
# Use Eclipse Temurin OpenJDK 17 as base image
### Multi-stage Dockerfile for Spring Boot Backend
# 1. Build stage: use Maven to build the JAR
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 2. Run stage: use a lightweight JRE image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/player-service-java-0.0.1-SNAPSHOT.jar app.jar
# Copy Player.csv for H2 database initialization
COPY Player.csv Player.csv
EXPOSE 8080
ENV JAVA_OPTS=""
ENV OLLAMA_HOST=http://ollama:11434/
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
