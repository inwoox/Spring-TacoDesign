FROM openjdk
ENV SPRING_PROFILES_ACTIVE docker
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

# sudo ./mvnw package dockerfile:build                                               / docker image build
# docker run --name taco -p 8080:8080 -d taco-cloud/microservice-distribute-docker   / docker container run