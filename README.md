# 🚌 Fleet Management API

API REST para la gestión de buses desarrollada con **Spring Boot 3** y **Java 17**, implementando **Clean Architecture** y usando **PostgreSQL** como base de datos.

## 📋 Descripción del Proyecto

Esta API fue desarrollada como parte de una prueba técnica para CIVA, implementando un sistema completo de gestión de buses con las siguientes funcionalidades:

### ✨ Características Principales

- **Gestión completa de buses** (CRUD completo)
- **Gestión de marcas de buses** (CRUD completo)
- **Autenticación y autorización** con JWT
- **Paginación** en listados
- **Documentación automática** con OpenAPI/Swagger
- **Arquitectura limpia** (Clean Architecture)
- **Base de datos relacional** PostgreSQL
- **Validaciones robustas** de datos
- **Manejo de errores** centralizado

## 🚀 Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.3.8**
- **Spring Security 6** (JWT Authentication)
- **Spring Data JPA**
- **PostgreSQL**
- **Hibernate**
- **Maven**
- **OpenAPI 3** (Swagger)
- **Lombok**

## 📊 Modelo de Datos

### Bus
```
- ID (Long, autogenerado)
- Número de bus (String, único)
- Placa (String, único)
- Fecha de creación (DateTime, autogenerada)
- Características (String)
- Marca de bus (Relación con BusBrand)
- Estado (ACTIVE/INACTIVE)
```

### Marca de Bus (BusBrand)
```
- ID (Long, autogenerado)
- Nombre (String, único)
- Fecha de creación (DateTime, autogenerada)
- Estado (activo/inactivo)
```

### Usuario (User)
```
- ID (Long, autogenerado)
- Username (String, único)
- Password (String, encriptado)
- Roles (Colección de roles)
```

## 🔗 Endpoints Principales

### 🔐 Autenticación
```http
POST /api/v1/authentication/sign-up    # Registro de usuario
POST /api/v1/authentication/sign-in    # Inicio de sesión
```

### 🚌 Gestión de Buses
```http
GET    /api/v1/buses                   # Listar todos los buses (paginado)
GET    /api/v1/buses/{id}              # Obtener bus por ID
POST   /api/v1/buses                   # Crear nuevo bus
PUT    /api/v1/buses/{id}              # Actualizar bus
DELETE /api/v1/buses/{id}              # Eliminar bus (lógico)
PATCH  /api/v1/buses/{id}/activate     # Activar bus
PATCH  /api/v1/buses/{id}/deactivate   # Desactivar bus
```

### 🏷️ Gestión de Marcas
```http
GET    /api/v1/bus-brands                        # Listar marcas (paginado)
GET    /api/v1/bus-brands/search                 # Buscar marcas
GET    /api/v1/bus-brands/{id}/dependencies      # Ver dependencias
POST   /api/v1/bus-brands                        # Crear marca
PUT    /api/v1/bus-brands/{id}                   # Actualizar marca
DELETE /api/v1/bus-brands/{id}                   # Eliminar marca
DELETE /api/v1/bus-brands/{id}/force             # Forzar eliminación
```

## 🛠️ Configuración e Instalación

### Prerrequisitos
- Java 17 o superior
- Maven 3.6+
- PostgreSQL 12+
- Git

### 1. Clonar el repositorio
```bash
git clone https://github.com/aldobal/civa-back.git
cd civa-back
```

### 2. Configurar la base de datos
Crear una base de datos PostgreSQL llamada `civa_db`:

```sql
CREATE DATABASE civa_db;
CREATE USER civa_user WITH PASSWORD 'civa_password';
GRANT ALL PRIVILEGES ON DATABASE civa_db TO civa_user;
```

### 3. Configurar application.properties
```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/civa_db
spring.datasource.username=civa_user
spring.datasource.password=civa_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Server Configuration
server.port=8080

# JWT Configuration
authorization.jwt.secret=mySecretKey
authorization.jwt.expiration.days=7
```

### 4. Ejecutar la aplicación
```bash
# Compilar el proyecto
./mvnw clean compile

# Ejecutar la aplicación
./mvnw spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## 📖 Documentación de la API

Una vez ejecutada la aplicación, puedes acceder a la documentación interactiva:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## 🔒 Autenticación

La API utiliza **JWT (JSON Web Tokens)** para la autenticación. Para acceder a los endpoints protegidos:

### 1. Registrar un usuario
```bash
curl -X POST http://localhost:8080/api/v1/authentication/sign-up \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password123",
    "roles": ["ADMIN"]
  }'
```

### 2. Iniciar sesión
```bash
curl -X POST http://localhost:8080/api/v1/authentication/sign-in \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password123"
  }'
```

### 3. Usar el token en las peticiones
```bash
curl -X GET http://localhost:8080/api/v1/buses \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 📝 Ejemplos de Uso

### Crear una marca de bus
```bash
curl -X POST http://localhost:8080/api/v1/bus-brands \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Volvo"
  }'
```

### Crear un bus
```bash
curl -X POST http://localhost:8080/api/v1/buses \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "number": "B001",
    "licensePlate": "ABC-123",
    "brandId": 1,
    "features": "Aire acondicionado, WiFi, USB"
  }'
```

### Listar buses con paginación
```bash
curl "http://localhost:8080/api/v1/buses?page=0&size=10&sort=number&direction=ASC" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🏗️ Arquitectura del Proyecto

El proyecto sigue los principios de **Clean Architecture**:

```
src/main/java/com/civa/platform/
├── fleet/                          # Módulo de gestión de flota
│   ├── application/                # Capa de aplicación
│   │   └── internal/
│   │       ├── commandservices/    # Servicios de comandos
│   │       └── queryservices/      # Servicios de consulta
│   ├── domain/                     # Capa de dominio
│   │   ├── model/
│   │   │   ├── aggregates/         # Agregados
│   │   │   ├── commands/           # Comandos
│   │   │   ├── entities/           # Entidades
│   │   │   ├── queries/            # Consultas
│   │   │   └── valueobjects/       # Objetos de valor
│   │   └── services/               # Interfaces de servicios
│   ├── infrastructure/             # Capa de infraestructura
│   │   └── persistence/jpa/        # Repositorios JPA
│   └── interfaces/                 # Capa de interfaces
│       └── rest/                   # Controladores REST
├── iam/                            # Módulo de autenticación
└── shared/                         # Componentes compartidos
```

## ✅ Cumplimiento de Requerimientos

### Requerimientos Obligatorios ✅
- ✅ **Java 17** - Implementado
- ✅ **Spring Boot 3** - Versión 3.3.8
- ✅ **PostgreSQL** - Base de datos relacional
- ✅ **Tabla de marcas relacionada** - Implementado con Foreign Key
- ✅ **Campos requeridos del bus** - Todos implementados
- ✅ **Endpoints GET /bus y GET /bus/{id}** - Implementados y extendidos

### Requerimientos Opcionales ✅
- ✅ **Paginación** - Implementada en todos los listados
- ✅ **Seguridad** - JWT Authentication con Spring Security
- ✅ **CRUD completo** - Create, Read, Update, Delete para buses y marcas
- ✅ **Documentación** - OpenAPI/Swagger completa
- ✅ **Validaciones** - Validaciones robustas de datos
- ✅ **Manejo de errores** - Respuestas HTTP apropiadas

## 🚀 Características Adicionales

- **CORS configurado** para desarrollo frontend
- **Eliminación lógica** (soft delete) de registros
- **Validación de unicidad** de números de bus y placas
- **Estados de buses** (ACTIVE/INACTIVE)
- **Auditoría automática** (fechas de creación y modificación)
- **Manejo de dependencias** (no se puede eliminar una marca si tiene buses asociados)
- **Activación/desactivación** de buses via endpoints específicos

## 📞 Contacto

**Desarrollador**: Aldo  
**Email**: rbazan@civa.com.pe  
**Repositorio**: https://github.com/CodAress/civa-backend

---

🎯 **Prueba técnica completada exitosamente** - API lista para integración con frontend React