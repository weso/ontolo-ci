FROM maven:3.6.0-jdk-11-slim
COPY . .
EXPOSE 8090
RUN mkdir ~/.m2/
RUN cp settings.xml ~/.m2/
ENTRYPOINT ["mvn","clean","package","spring-boot:run"]
