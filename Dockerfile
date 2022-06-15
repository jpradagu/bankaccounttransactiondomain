FROM openjdk:11-jdk-slim

WORKDIR /opt/app

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "jar", "app.jar"]

