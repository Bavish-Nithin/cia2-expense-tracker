
# Stage 1: Build the Java application
FROM maven:3.9.9-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Compile and package the application
RUN mvn clean package

# Stage 2: Run the application
FROM eclipse-temurin:17-jre

# Set working directory
WORKDIR /app

# Copy the generated JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose application port
EXPOSE 8080

# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
