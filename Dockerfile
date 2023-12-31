FROM maven:3.8.3-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests=true

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/newsapp-0.0.1-SNAPSHOT.jar newsapp.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "newsapp.jar" ]
