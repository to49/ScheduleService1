FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/*.jar domen12-0.0.1-SNAPSHOT.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "domen12-0.0.1-SNAPSHOT.jar"]