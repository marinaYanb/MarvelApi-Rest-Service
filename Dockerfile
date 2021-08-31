FROM openjdk:11-jre-slim
COPY ./target/comics-project-0.0.1-SNAPSHOT.jar /comics-project/
WORKDIR /comics-project/
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "comics-project-0.0.1-SNAPSHOT.jar"]