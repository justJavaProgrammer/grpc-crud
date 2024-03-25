FROM gradle:7.2.0-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

EXPOSE 9090

FROM openjdk:17

WORKDIR /grpc/books

COPY . .

CMD ["./gradlew", "clean", "bootJar"]

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

