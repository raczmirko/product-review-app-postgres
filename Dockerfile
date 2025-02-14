# Use Maven as a builder image
FROM maven:3.9-eclipse-temurin-17 AS build

# Set working directory inside the container
WORKDIR /app

# Copy the source code
COPY . .

# Build the application
RUN mvn clean package -DskipTests=true

# Use a lightweight JDK image for running the application
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy the JAR from the previous build stage
COPY --from=build /app/target/product-review-app.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
