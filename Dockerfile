FROM openjdk:19
WORKDIR /app
COPY ./target/store-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
