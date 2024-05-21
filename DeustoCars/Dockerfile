# Use the official Maven image as the base image
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set the working directory
WORKDIR /DeustoCars

# Copy the project files to the working directory
COPY . /DeustoCars

# Execute the Maven build to compile and enhance the project
RUN mvn compile datanucleus:enhance

# Specify the command to run Jetty using Maven
CMD ["mvn", "jetty:run"]