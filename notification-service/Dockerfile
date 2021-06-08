FROM openjdk:11
ADD target/notification-service.jar /app/notification-service.jar
WORKDIR /app
EXPOSE 8091
ENTRYPOINT ["java", "-jar", "notification-service.jar"]


