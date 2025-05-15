
FROM maven:3.8.6-openjdk-8 AS build

WORKDIR /app

COPY . .


RUN mvn clean package -DskipTests


FROM openjdk:8-jdk-alpine

WORKDIR /app


COPY --from=build /app/target/test-project-0.0.1-SNAPSHOT.jar app.jar


ENTRYPOINT ["java", "-jar", "app.jar"]