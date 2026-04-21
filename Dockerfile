# syntax=docker/dockerfile:1

FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /workspace
COPY pom.xml .
COPY src ./src

RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre

WORKDIR /app
RUN useradd --system --create-home --uid 10001 appuser

COPY --from=builder /workspace/target/dental-surgery-appointment.jar /app/app.jar

EXPOSE 8080

ENV JAVA_OPTS=""

USER appuser
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
