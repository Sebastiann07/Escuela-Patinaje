#!/usr/bin/env bash
set -e

# Si este script está en la raíz del repo, entra a v1
if [ -d "v1" ]; then
  cd v1
fi

# Asegura permiso ejecutable en el wrapper (solución temporal)
chmod +x ./mvnw || true

# Build (usa el wrapper incluido)
./mvnw -B package -DskipTests

# Ejecuta el JAR producido (usa PORT de Railway)
exec bash -lc "java -jar target/*.jar --server.port=${PORT:-8080}"