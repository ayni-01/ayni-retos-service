# 🏆 Ayni Retos Service

Microservicio que implementa el **Core Domain** de la plataforma **Somos Ayni**. En lugar de pedir un CV clásico, una empresa define un problema práctico con entregables y recompensas concretas, y los talentos lo resuelven para demostrar su capacidad. Este servicio modela todo el ciclo de vida de esos retos, desde su creación hasta su cierre o archivo.

## Responsabilidad del Bounded Context

**Maneja:**
- Publicación, edición y gestión del ciclo de vida de retos (BORRADOR → ACTIVO → CERRADO → ARCHIVADO)
- Duplicación de retos existentes como punto de partida para nuevos
- Búsqueda y filtrado de retos por categoría, nivel de dificultad, empresa y estado

**NO maneja:**
- Postulaciones ni evaluaciones de talentos (eso es responsabilidad de `postulaciones-service`)
- Perfiles de quienes crean o resuelven los retos (`perfiles-service`)
- Notificaciones al cerrar un reto (`notificaciones-service` se encarga de eso)

---

## Endpoints REST

| Método | Ruta | Descripción | Auth |
|--------|------|-------------|------|
| `POST` | `/api/v1/retos` | Publicar un nuevo reto | JWT (EMPRESA) |
| `POST` | `/api/v1/retos/{id}/duplicar` | Duplicar un reto existente como borrador | JWT (EMPRESA, dueña) |
| `POST` | `/api/v1/retos/{id}/cerrar` | Cerrar un reto activo | JWT (EMPRESA, dueña) |
| `POST` | `/api/v1/retos/{id}/archivar` | Archivar un reto cerrado | JWT (EMPRESA, dueña) |
| `GET` | `/api/v1/retos` | Buscar retos con filtros opcionales | JWT |
| `GET` | `/api/v1/retos/{id}` | Obtener detalle de un reto por ID | JWT |

### Body de `POST /api/v1/retos` — `PublicarRetoRequest`

```json
{
  "titulo": "Diseña una landing page con React",
  "descripcion": "Descripción detallada del problema a resolver",
  "categoria": "FRONTEND",
  "nivelDificultad": "JUNIOR",
  "recompensa": "CONTRATACION",
  "empresaId": "uuid-de-la-empresa",
  "fechaLimite": "2026-07-31"
}
```

### Query params de `GET /api/v1/retos`

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `categoria` | `String` | Filtrar por categoría técnica |
| `nivelDificultad` | `String` | Filtrar por nivel requerido |
| `empresaId` | `UUID` | Filtrar por empresa publicadora |
| `estado` | `String` | Filtrar por estado del reto |

---

## Arquitectura (Hexagonal)

```
src/main/java/com/somosayni/retos/
├── domain/
│   ├── model/          # Agregado Reto, value objects, enums
│   ├── port/
│   │   ├── in/         # Casos de uso (interfaces de entrada)
│   │   └── out/        # Repositorios (interfaces de salida)
│   └── exception/      # Excepciones de dominio (RetoNoEncontrado, TransicionInvalida, etc.)
├── application/
│   └── service/        # Implementaciones de los casos de uso
└── infrastructure/
    ├── adapter/
    │   ├── in/rest/    # Controladores REST, DTOs de entrada/salida
    │   └── out/jpa/    # Repositorios JPA, entidades de BD, mappers
    └── config/         # Spring Security, Swagger, beans de configuración
```

- **Domain:** Contiene el núcleo del negocio. El agregado `Reto` conoce sus propias reglas de transición de estado y no depende de ningún framework.
- **Application:** Orquesta los casos de uso (publicar, duplicar, cerrar, archivar) coordinando dominio y puertos de salida.
- **Infrastructure:** Adaptadores concretos: controladores HTTP con Spring MVC, persistencia con Spring Data JPA, filtros JWT para seguridad.

---

## Modelos de dominio principales

### Reto (Agregado raíz)

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | `UUID` | Identificador único |
| `titulo` | `String` | Título descriptivo del reto |
| `descripcion` | `String` | Detalle del problema a resolver |
| `categoria` | `Categoria` | Área técnica del reto |
| `nivelDificultad` | `NivelDificultad` | Nivel de experiencia requerido |
| `recompensa` | `TipoRecompensa` | Tipo de retribución al ganador |
| `empresaId` | `UUID` | Referencia a la empresa publicadora |
| `estado` | `EstadoReto` | Estado actual en el ciclo de vida |
| `fechaLimite` | `LocalDate` | Fecha límite de entrega |
| `fechaCreacion` | `LocalDateTime` | Timestamp de creación |

### Enum: `EstadoReto` — Ciclo de vida

```
BORRADOR → ACTIVO → CERRADO → ARCHIVADO
```

| Estado | Descripción |
|--------|-------------|
| `BORRADOR` | Creado pero aún no publicado |
| `ACTIVO` | Visible y abierto para postulaciones |
| `CERRADO` | Ya no acepta nuevas postulaciones |
| `ARCHIVADO` | Retirado del catálogo público |

### Enum: `Categoria`

`FRONTEND` · `BACKEND` · `FULLSTACK` · `DATA` · `DEVOPS` · `UX_UI` · `QA` · `MOBILE`

### Enum: `NivelDificultad`

`TRAINEE` · `JUNIOR` · `SENIOR`

### Enum: `TipoRecompensa`

`MONETARIA` · `CONTRATACION` · `DIPLOMA`

---

## Cómo ejecutar

### Local

```bash
# Requisitos: Java 21, Maven 3.9+, PostgreSQL 15+
# La base de datos 'somosayni' debe existir antes de iniciar

mvn clean package -DskipTests
java -jar target/*.jar
```

### Docker

```bash
cp .env.example .env
# Editar .env con tus valores reales
docker-compose up --build
```

---

## Variables de entorno

| Variable | Descripción | Valor por defecto |
|----------|-------------|-------------------|
| `SPRING_DATASOURCE_URL` | URL JDBC de la base de datos | `jdbc:postgresql://localhost:5432/somosayni` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de PostgreSQL | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de PostgreSQL | *(obligatorio)* |
| `JWT_SECRET` | Clave secreta compartida para validar JWT | *(obligatorio)* |
| `SERVER_PORT` | Puerto en que levanta el servicio | `8083` |

---

## Swagger / OpenAPI

| | Link |
|---|---|
| **Swagger UI (local)** | [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html) |
| **OpenAPI JSON (local)** | [http://localhost:8083/api-docs](http://localhost:8083/api-docs) |
| **swagger.json (repo)** | [ver en GitHub](https://github.com/ayni-01/ayni-retos-service/blob/main/swagger.json) |
| **Swagger Editor (online)** | [abrir en Swagger Editor](https://editor.swagger.io/?url=https://raw.githubusercontent.com/ayni-01/ayni-retos-service/main/swagger.json) |

> Para probar los endpoints protegidos: copia el JWT del login → clic en **Authorize** → pega `Bearer <tu-token>`.

---

## Dependencias

Este servicio forma parte del ecosistema **Somos Ayni** y comparte las siguientes convenciones:

- **Base de datos compartida:** Usa el esquema PostgreSQL `somosayni`. Cada microservicio opera sobre sus propias tablas dentro de la misma instancia.
- **Autenticación JWT:** Valida tokens firmados por `identidad-service` usando la misma clave `JWT_SECRET`. No genera tokens propios.
- **Comunicación entre servicios:** Los microservicios son independientes entre sí. Se referencian únicamente por ID (p. ej. `empresaId`), nunca por llamadas directas entre servicios en el flujo normal.
