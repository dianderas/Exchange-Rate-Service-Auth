# Etapa 1: Construcción
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

COPY ../pom.xml .
COPY . .

RUN mvn -B dependency:go-offline
RUN mvn -B package -DskipTests

# Etapa 2: Imagen final
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

COPY --from=builder /app/gateway-service/target/gateway-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]