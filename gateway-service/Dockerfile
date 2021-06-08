FROM openjdk:11
ADD target/gateway-service.jar /app/gateway-service.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "gateway-service.jar"]


