# 🛼 Escuela de Patinaje – Guía del Proyecto
Sistema de gestión de una escuela de patinaje desarrollado con Spring Boot y vistas Thymeleaf. Este documento sigue la estructura solicitada, adaptando el contenido real del proyecto.

📋 Tabla de Contenidos
- Integrantes
- Tecnologías
- Arquitectura del Sistema
- Documentación Detallada
- Instalación y Ejecución
- API Endpoints
- Estructura del Proyecto

👥 Integrantes
- Sebastián David Sánchez Parra
- Miguel Angel Quintero Jaramillo
- Estefania Yepes Lopera
- Pablo Andres Gomez Estrada

🛠 Tecnologías
- Backend: Java 17 + Spring Boot
- Vistas: Thymeleaf
- Frontend: Bootstrap (CSS framework)
- Persistencia: Spring Data JPA (si aplica en tu configuración)
- Documentación: Markdown
- Patrones: MVC, Service Layer, Repository Pattern

🏗 Arquitectura del Sistema
Arquitectura en capas orientada a MVC:

┌─────────────────┐    ┌─────────────────┐    ┌─────────────────────┐
│   Presentación  │    │     Dominio     │    │   Persistencia      │
│  (Controllers & │────│  (Servicios y   │────│ (Repositorios/Data  │
│   Thymeleaf)    │    │   Modelos)      │    │ Sources/JPA)        │
│                 │    │                 │    │                     │
│ - Controladores │    │ - Services      │    │ - Repositories      │
│ - Templates     │    │ - Models        │    │ - MySql             │
└─────────────────┘    └─────────────────┘    └─────────────────────┘

🎯 Roles del Sistema
- Admin/Staff: Gestión de clases, instructores y usuarios.
- Alumno: Registro y acceso al dashboard.

📚 Documentación Detallada
Documento	Descripción
- 🏛 Arquitectura: Capas (Presentación, Dominio, Persistencia) y responsabilidades.
- 👥 Dominio y Servicios: Reglas de negocio y servicios (service/).
- 🗄 Persistencia y JPA: Repositorios (repository/) y entidades (model/) si están definidos.
- 🌐 Vistas y Fragmentos: Organización de templates y uso de Bootstrap.
- 🌐 API/Endpoints: Rutas HTML y JSON por módulo.
- ⚙ Configuración: application.properties y ejecución.

🚀 Instalación y Ejecución
Prerrequisitos
- Java 17+
- Maven 3.6+

Pasos de Instalación
1) Clonar el repositorio

git clone <repository-url>
cd Escuela-Patinaje/v1

2) Configurar application.properties
- server.port=8080
- (Opcional) spring.datasource.*, spring.jpa.* si usas BD
3) Ejecutar la aplicación

./mvnw spring-boot:run

4) Verificar funcionamiento
- http://localhost:8080
- Vistas principales: /index/, /login, /clases/listar

🌐 API Endpoints

Index
- GET /index/ → Renderiza templates/index.html
- GET /index/home → Renderiza templates/index.html

Autenticación (AuthController)
- GET /login → Muestra auth/login
- GET /registro → Muestra auth/registro
- POST /registro → Crea usuario (email único, password encriptado, rol ALUMNO). Redirige a /login
- GET /dashboard → Muestra auth/dashboard

Usuarios – HTML (UserController)
- GET /users/listar → Lista usuarios en users/list
- GET /users/ver/{id} → Detalle en users/detail
- GET /users/crear → Form en users/form
- POST /users/guardar → Crea usuario (email único)
- GET /users/editar/{id} → Form de edición
- POST /users/actualizar/{id} → Actualiza usuario
- POST /users/eliminar/{id} → Elimina usuario

Usuarios – API JSON
- GET /users/api/json → { timestamp, status, data:[userModel], total, path }
- GET /users/api/json/{id} → { status, data:userModel } | 404 si no existe

Instructores – HTML (instructorController)
- GET /instructores/listar → Lista
- GET /instructores/ver/{id} → Detalle
- GET /instructores/crear → Form
- POST /instructores/guardar → Crea (email único)
- GET /instructores/editar/{id} → Form de edición
- POST /instructores/actualizar/{id} → Actualiza
- POST /instructores/eliminar/{id} → Elimina

Instructores – API JSON
- GET /instructores/api/json → { timestamp, status, data:[instructorModel], total, path }
- GET /instructores/api/json/{id} → { status, data:instructorModel } | 404 si no existe

Clases – HTML (claseController)
- GET /clases/listar → Lista
- GET /clases/ver/{id} → Detalle
- GET /clases/crear → Form (con instructores activos)
- POST /clases/guardar → Crea
- GET /clases/editar/{id} → Form de edición
- POST /clases/actualizar/{id} → Actualiza
- POST /clases/eliminar/{id} → Elimina

Clases – API JSON
- GET /clases/api/json → { timestamp, status, data:[claseModel], total, path }
- GET /clases/api/json/{id} → { status, data:claseModel } | 404 si no existe

📁 Estructura del Proyecto

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


📄 Licencia
- Este proyecto no declara licencia

🛼 Escuela de Patinaje – Construido con Spring Boot, Thymeleaf y Bootstrap