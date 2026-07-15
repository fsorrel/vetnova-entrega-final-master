# syntax=docker/dockerfile:1
# ---- Etapa 1: build (compila el .jar con Maven + JDK 17) ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -B -q dependency:go-offline || true
COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ---- Etapa 2: runtime (solo JRE 17 + el .jar) ----
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
