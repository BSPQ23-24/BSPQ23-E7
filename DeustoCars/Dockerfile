# Specifies the base image
FROM maven:3.8.1-openjdk-11
# Copy the source code to the image
COPY . /spq-simple-service
# Execute the maven build
RUN cd spq-simple-service && mvn compile datanucleus:enhance
# change to this directory
WORKDIR spq-simple-service
# Specify which command will be launched when starting the container
CMD ["mvn", "jetty:run"]

