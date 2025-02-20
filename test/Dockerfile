FROM eclipse-temurin:21-jdk AS builder

# Install dependencies for live reloading
RUN apt-get update && apt-get install -y maven

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and package the app for development
COPY src /app/src

# Pass environment variables as JVM arguments and activate the test profile
CMD ["sh", "-c", "mvn test -Dspring.profiles.active=test"]