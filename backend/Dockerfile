# Use OpenJDK 21 as the base image for building
FROM openjdk:21-jdk-slim AS build

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set the working directory
WORKDIR /app

# Copy the project files
COPY . .

# Run Maven build inside the container
RUN mvn clean package -DskipTests

# Use a separate, smaller runtime image
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=build /app/target/product-management-app-1.0-SNAPSHOT.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
