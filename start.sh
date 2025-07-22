#!/bin/bash
# Script de inicio para Render

echo "🚀 Iniciando aplicación SIME Backend..."

# Construir la aplicación
echo "📦 Construyendo aplicación..."
./mvnw clean package -DskipTests

# Ejecutar la aplicación
echo "▶️ Ejecutando aplicación..."
java -Dserver.port=$PORT -jar target/backwebsime-0.0.1-SNAPSHOT.jar
