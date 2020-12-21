FROM openjdk:11
FROM maven:3.6.0-jdk-11-slim
COPY . .
EXPOSE 8081
WORKDIR ontoloci-server-igniter
ENTRYPOINT ["mvn","spring-boot:run"]