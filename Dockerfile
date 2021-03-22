FROM maven:3.6.0-jdk-11-slim
COPY . .
EXPOSE 8090
ENTRYPOINT ["mvn","clean","package","spring-boot:run"]
