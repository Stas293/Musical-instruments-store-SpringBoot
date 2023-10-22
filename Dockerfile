FROM maven:3.9.3 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM openjdk:21
COPY --from=build /home/app/target/store-0.0.1-SNAPSHOT.jar /usr/local/lib/store-0.0.1-SNAPSHOT.jar
EXPOSE 8080

WORKDIR /usr/local/lib
ENTRYPOINT ["java","-jar","/usr/local/lib/store-0.0.1-SNAPSHOT.jar"]
