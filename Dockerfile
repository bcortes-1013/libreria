# ============================================================
# Dockerfile - Backend Spring Boot (Java 17)
# ============================================================

# Imagen base ligera con Java 17
FROM eclipse-temurin:17-jdk-alpine

# Carpeta de trabajo dentro del contenedor
WORKDIR /app

# Copiar el JAR generado por Maven
COPY target/libreria-0.0.1-SNAPSHOT.jar app.jar

# Copiar carpeta Wallet
COPY Wallet /app/wallet

# Exponer el puerto del backend
EXPOSE 8080

# Ejecutar Spring Boot con perfil DOCKER
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
