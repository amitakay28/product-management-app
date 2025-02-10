# Use OpenJDK 21
FROM openjdk:21-jdk-slim

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set the working directory
WORKDIR /app

# Copy the project files
COPY . .

# Run Maven build inside the container
RUN mvn clean package -DskipTests

# Copy the generated JAR
COPY target/product-management-app-1.0-SNAPSHOT.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
