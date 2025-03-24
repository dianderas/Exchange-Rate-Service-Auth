# Etapa 1: Construcción
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copia el POM raíz para resolver dependencias
COPY ../pom.xml .

# Copia el código del módulo
COPY . .

# Descarga dependencias
RUN mvn -B dependency:go-offline

# Construye solo este servicio
RUN mvn -B package -DskipTests

# Etapa 2: Imagen final
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Copia solo el JAR del servicio
COPY --from=builder /app/authorization-service/target/authorization-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9000
ENTRYPOINT ["java", "-jar", "app.jar"]