# Use the official Gradle image as a build image
FROM gradle:6.8.3-jdk8 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the build.gradle and settings.gradle files to the container
COPY main/build.gradle .
COPY main/settings.gradle .

# Copy the source code
COPY main/src/ src/

# Build the application
RUN gradle build --no-daemon

# Create a new stage with the application JAR and runtime dependencies
FROM openjdk:8

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage to the current stage
COPY --from=build /app/build/libs/main-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that the application will run on
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "app.jar"]
