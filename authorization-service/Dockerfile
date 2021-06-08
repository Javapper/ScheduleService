FROM openjdk:8
ADD target/authorization-service.jar /app/authorization-service.jar
WORKDIR /app
EXPOSE 8092
ENTRYPOINT ["java", "-jar", "authorization-service.jar"]


