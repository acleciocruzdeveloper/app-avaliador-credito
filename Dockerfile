FROM maven:3.9-amazoncorretto-17 as build
WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM amazoncorretto:17-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

#CONFIG EUREKA
ARG EUREKA_SERVER=localhost

ARG USER_EUREKA=user
ARG PASSWORD_EUREKA=1234567890

#CONFIG RABBITMQ
ARG RABBIT_HOST=rabbitmq-host
ARG RABBITMQ_USERNAME=rabbitmq-username
ARG RABBITMQ_PASSWORD=rabbitmq-pass

ENTRYPOINT ["java", "-jar", "app.jar"]
