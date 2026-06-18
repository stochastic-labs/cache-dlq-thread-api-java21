FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.32.0/opentelemetry-javaagent.jar /build/opentelemetry-javaagent.jar

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

COPY --from=builder /build/opentelemetry-javaagent.jar opentelemetry-javaagent.jar

EXPOSE 8082

ENTRYPOINT ["java", "-javaagent:opentelemetry-javaagent.jar", "-jar", "app.jar"]