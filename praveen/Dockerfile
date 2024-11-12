# Use a Java image for Spring Boot
FROM openjdk:17-jdk-slim

# Set up a volume to store temporary files
VOLUME /tmp

# Copy your app's .jar file into the container
COPY target/praveen-0.0.1-SNAPSHOT.jar app.jar

# Run the app when the container starts
ENTRYPOINT ["java", "-jar", "/app.jar"]

# Expose port 8080 so we can access it from outside
EXPOSE 8080