Api documentación: http://localhost:8080/swagger-ui.html

# 🛼 Escuela de Patinaje - Guía del Proyecto (Estructura Adaptada)

Sistema de gestión de escuela de patinaje desarrollado con **Spring Boot** y **Thymeleaf**, organizado por capas (Controllers, Services, Repositories, Models) y usando **Bootstrap** en las vistas.

## 📋 Tabla de Contenidos
- [Integrantes](#integrantes)
- [Tecnologías](#tecnologías)
- [Arquitectura del Sistema](#arquitectura-del-sistema)
- [Documentación Detallada](#documentación-detallada)
- [Instalación y Ejecución](#instalación-y-ejecución)
- [API Endpoints](#api-endpoints)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Licencia](#licencia)

## 👥 Integrantes
- **Sebastián David Sánchez Parra**
- **Miguel Angel Quintero Jaramillo**
- **Estefania Yepes Lopera**
- **Pablo Andres Gomez Estrada**

## 🛠️ Tecnologías
- **Backend**: Java 17 + Spring Boot
- **Frontend**: Thymeleaf + Bootstrap
- **Persistencia**: Spring Data JPA (si aplica)
- **Documentación**: Markdown
- **Patrones**: MVC, Service Layer, Repository Pattern

## 🏗️ Arquitectura del Sistema
Arquitectura en **capas (MVC)**:
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────────┐
│   Presentación  │    │     Dominio     │    │   Persistencia      │
│  (Controllers & │────│  (Servicios y   │────│ (Repositorios/Data  │
│   Thymeleaf)    │    │   Modelos)      │    │ Sources/JPA)        │
│                 │    │                 │    │                     │
│ - Controladores │    │ - Services      │    │ - Repositories      │
│ - Templates     │    │ - Models        │    │ - MySql             │
└─────────────────┘    └─────────────────┘    └─────────────────────┘
```
- Presentación: Controladores web y vistas Thymeleaf (con Bootstrap).
- Dominio: Servicios y modelos.
- Persistencia: Repositorios (Spring Data) y configuración BD (si está activa).

## 📚 Documentación Detallada
| Documento | Descripción |
|-----------|-------------|
| Arquitectura | Capas y responsabilidades (MVC) |
| Dominio y Servicios | Reglas de negocio y servicios (`service/`) |
| Persistencia y JPA | Repositorios (`repository/`) y modelos (`model/`) |
| Vistas y Fragmentos | Templates Thymeleaf y fragmentos (`fragments/`) con Bootstrap |
| API/Endpoints | Rutas HTML y JSON por módulo |
| Configuración | `application.properties`, puertos y BD |

## 🚀 Instalación y Ejecución

### Prerrequisitos
- Java 17+
- Maven 3.6+

### Pasos de Instalación
1. **Clonar el repositorio**
```
git clone <repository-url>
cd Escuela-Patinaje/v1
```
2. **Configurar `application.properties`**
```
server.port=8080
# Opcional si usas BD
# spring.datasource.url=jdbc:mysql://localhost:3306/escuela_patinaje
# spring.datasource.username=<user>
# spring.datasource.password=<pass>
# spring.jpa.hibernate.ddl-auto=update
```
3. **Ejecutar la aplicación**
```
./mvnw spring-boot:run
```
4. **Verificar funcionamiento**
- Servidor: `http://localhost:8080`
- Vistas: `/index/`, `/login`, `/clases/listar`

## 🌐 API Endpoints

### Index
- `GET /index/` – Renderiza `templates/index.html`
- `GET /index/home` – Renderiza `templates/index.html`

### Autenticación (`AuthController`)
- `GET /login` – `auth/login`
- `GET /registro` – `auth/registro`
- `POST /registro` – Crea usuario (email único, password encriptado, rol ALUMNO). Redirige a `/login`
- `GET /dashboard` – `auth/dashboard`

### Usuarios – HTML (`UserController`)
- `GET /users/listar` – Lista (`users/list`)
- `GET /users/ver/{id}` – Detalle (`users/detail`)
- `GET /users/crear` – Form (`users/form`)
- `POST /users/guardar` – Crear
- `GET /users/editar/{id}` – Editar
- `POST /users/actualizar/{id}` – Actualizar
- `POST /users/eliminar/{id}` – Eliminar

### Usuarios – API JSON
- `GET /users/api/json` – `{ timestamp, status, data:[userModel], total, path }`
- `GET /users/api/json/{id}` – `{ status, data:userModel }` | 404 si no existe

### Instructores – HTML (`instructorController`)
- `GET /instructores/listar` – Lista
- `GET /instructores/ver/{id}` – Detalle
- `GET /instructores/crear` – Form
- `POST /instructores/guardar` – Crear (email único)
- `GET /instructores/editar/{id}` – Editar
- `POST /instructores/actualizar/{id}` – Actualizar
- `POST /instructores/eliminar/{id}` – Eliminar

### Instructores – API JSON
- `GET /instructores/api/json` – `{ timestamp, status, data:[instructorModel], total, path }`
- `GET /instructores/api/json/{id}` – `{ status, data:instructorModel }` | 404 si no existe

### Clases – HTML (`claseController`)
- `GET /clases/listar` – Lista
- `GET /clases/ver/{id}` – Detalle
- `GET /clases/crear` – Form (con instructores activos)
- `POST /clases/guardar` – Crear
- `GET /clases/editar/{id}` – Editar
- `POST /clases/actualizar/{id}` – Actualizar
- `POST /clases/eliminar/{id}` – Eliminar

### Clases – API JSON
- `GET /clases/api/json` – `{ timestamp, status, data:[claseModel], total, path }`
- `GET /clases/api/json/{id}` – `{ status, data:claseModel }` | 404 si no existe

## 📁 Estructura del Proyecto
```
v1/
  mvnw, mvnw.cmd
  pom.xml
  src/
    main/
      java/
        com/patinaje/v1/
          V1Application.java
          config/
          controller/
            AuthController.java
            UserController.java
            instructorController.java
            claseController.java
            indexController.java
          model/
          repository/
          service/
      resources/
        application.properties
        static/
          images/
        templates/
          index.html
          auth/
          clases/
          instructores/
          users/
          fragments/
          pages/
    test/
      java/com/patinaje/v1/
  target/
```

## 📄 Licencia
- Este proyecto no declara licencia. Añade `LICENSE` si deseas especificarla.
