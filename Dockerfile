FROM openjdk:17-jdk-slim

ARG JAR_FILE=build/libs/*-SNAPSHOT.jar

COPY $JAR_FILE app.jar

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-Dspring.profiles.active=dev", "-jar", "/app.jar"]
