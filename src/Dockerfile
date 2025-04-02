FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the JAR file
COPY ./target/bff-mobile-1.0-SNAPSHOT.jar app.jar

# Expose the port your application runs on (adjust if needed)
EXPOSE 8080

# Command to run your application
CMD ["java", "-jar", "app.jar"]