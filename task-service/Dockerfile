FROM openjdk:11
ADD target/task-service.jar /app/task-service.jar
WORKDIR /app
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "task-service.jar"]


