FROM maven:3.9.9-amazoncorretto-21-alpine AS compiler
WORKDIR /app
COPY pom.xml .
COPY src src
RUN mvn clean install -DskipTests -T 4

FROM amazoncorretto:21-alpine-jdk as builder
WORKDIR /app
COPY --from=compiler /app/target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM amazoncorretto:21-alpine-jdk
WORKDIR application
COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
