# Dockerfile for Spring Boot Backend
# Use Eclipse Temurin OpenJDK 17 as base image
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Copy the built jar file
COPY target/player-service-java-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Set environment variable for Java options (optional)
ENV JAVA_OPTS=""

# Set environment variable for OLLAMA_HOST (can be overridden by docker-compose)
ENV OLLAMA_HOST=http://ollama:11434/

# Run the Spring Boot application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
