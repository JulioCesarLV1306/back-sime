# Instrucciones para prueba local con PostgreSQL

## 1. Instalar PostgreSQL localmente
Descarga desde: https://www.postgresql.org/download/windows/

## 2. Crear base de datos local
```bash
# Conectar a PostgreSQL
psql -U postgres

# Crear base de datos
CREATE DATABASE backendwebsime;

# Conectar a la base de datos
\c backendwebsime

# Ejecutar el script de migración
\i database-migration.sql
```

## 3. Configurar variables de entorno para desarrollo local
En tu IDE o en el archivo application-dev.properties:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/backendwebsime
spring.datasource.username=postgres
spring.datasource.password=tu_password_postgres
```

## 4. Probar la aplicación
```bash
./mvnw spring-boot:run
```
