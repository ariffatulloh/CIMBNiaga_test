# Use a base image with Java and Maven installed
FROM adoptopenjdk:11-jdk-hotspot as builder

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project file
COPY pom.xml .

# Download the project dependencies
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -B

# Copy the application source code
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Use a lightweight base image for running the application
FROM adoptopenjdk:11-jre-hotspot

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=builder /app/target/*.jar app.jar

# Specify the command to run the application
CMD ["java", "-jar", "app.jar"]