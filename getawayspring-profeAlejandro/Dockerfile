# syntax=docker/dockerfile:1
# ---- Etapa 1: build (compila el .jar con Maven + JDK 25) ----
# Nota: el Gateway usa Spring Boot 4 / Java 25. Si el tag no estuviera disponible,
# usar "maven:3-eclipse-temurin-25".
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -B -q dependency:go-offline || true
COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ---- Etapa 2: runtime (solo JRE 25 + el .jar) ----
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
