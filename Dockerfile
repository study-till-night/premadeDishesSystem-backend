FROM maven:3.5-jdk-17-alpine as builder
# Copy local code to the container image.
ENV PATH /server
ENV APP_PATH  /server/target/premade_dished_system.jar
WORKDIR $PATH
COPY pom.xml .
COPY src ./src
# Build a release artifact.
RUN mvn package -DskipTests
# Run the web service on container startup.
CMD ["java","-jar","echo $APP_PATH","--spring.profiles.active=prod"]