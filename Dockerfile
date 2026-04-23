# ─── STAGE 1: BUILD ───────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /app

# Copia primero solo los archivos de dependencias para aprovechar cache de Docker
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B --no-transfer-progress

# Ahora copia el código fuente y compila
COPY src ./src
RUN ./mvnw package -DskipTests -B --no-transfer-progress

# ─── STAGE 2: RUN ─────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /app/target/tienda-bicicletas-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
