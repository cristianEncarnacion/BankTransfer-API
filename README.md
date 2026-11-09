# 🏦 BankTransfer API

[![CI](https://github.com/cristianEncarnacion/BankTransfer-API/actions/workflows/ci.yml/badge.svg)](https://github.com/cristianEncarnacion/BankTransfer-API/actions/workflows/ci.yml)

API REST de transferencias bancarias construida con **Quarkus**, orientada a eventos con **Kafka**, persistencia en **PostgreSQL**, caché distribuida con **Redis** y seguridad con **Keycloak (OIDC)**. Todo el entorno se levanta con **Docker Compose**.

![Java](https://img.shields.io/badge/Java_21-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Quarkus](https://img.shields.io/badge/Quarkus_3.15-4695EB?style=flat&logo=quarkus&logoColor=white)
![Kafka](https://img.shields.io/badge/Apache_Kafka-231F20?style=flat&logo=apachekafka&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL_16-4169E1?style=flat&logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat&logo=redis&logoColor=white)
![Keycloak](https://img.shields.io/badge/Keycloak-4D4D4D?style=flat&logo=keycloak&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white)

---

## 🧭 Arquitectura

```
                ┌──────────────┐   JWT (OIDC)   ┌────────────┐
  Cliente ────► │ TransferAPI  │ ◄────────────► │  Keycloak  │
                │  (Quarkus)   │                └────────────┘
                └──────┬───────┘
          persist      │      cache (@CacheResult / @CacheInvalidateAll)
     ┌─────────────────┼──────────────────┐
     ▼                 ▼                  ▼
┌──────────┐   ┌──────────────────┐   ┌──────────┐
│PostgreSQL│   │   Kafka topic    │   │  Redis   │
│ (Flyway) │   │ "transferencias" │   └──────────┘
└──────────┘   └────────┬─────────┘
                        ▼
                NotificacionConsumer
```

1. El cliente se autentica contra **Keycloak** y llama a la API con un JWT (rol `cliente_banca`).
2. `POST /transferencias` guarda la transferencia en **PostgreSQL** dentro de una transacción.
3. Se publica un `TransferEvent` en el topic **`transferencias`** de Kafka.
4. `NotificacionConsumer` consume el evento y procesa la notificación de forma asíncrona.
5. Las consultas `GET` se cachean en **Redis** (TTL de 5 min) y la caché se invalida al crear una transferencia.

## ✨ Características

- **Event-driven**: desacople entre el registro de la transferencia y las notificaciones usando SmallRye Reactive Messaging + Kafka.
- **Seguridad**: endpoints protegidos con OIDC/Keycloak y control de acceso por roles (`@RolesAllowed`).
- **Caché distribuida**: Redis con `@CacheResult` / `@CacheInvalidateAll` y expiración configurable.
- **Migraciones versionadas** con Flyway (`db/migration`).
- **Persistencia** con Hibernate ORM + Panache.
- **Contraseñas hasheadas** con BCrypt.
- **Contenerizado**: PostgreSQL, Kafka, Zookeeper, Redis y Keycloak en un solo `docker-compose`.

## 📡 Endpoints

Todos requieren un JWT válido con el rol `cliente_banca`.

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/transferencias` | Crea una transferencia y publica el evento en Kafka |
| `GET` | `/transferencias` | Lista todas las transferencias (cacheado) |
| `GET` | `/transferencias/{id}` | Obtiene una transferencia por ID (cacheado) |

**Ejemplo de request:**

```bash
curl -X POST http://localhost:8081/transferencias \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"cuentaOrigen":"1001234567","cuentaDestino":"2009876543","monto":1500.00}'
```

## 🚀 Cómo correrlo

**Requisitos:** Java 21, Maven (o el wrapper incluido) y Docker.

```bash
# 1. Levantar infraestructura (PostgreSQL, Kafka, Redis, Keycloak)
docker compose up -d

# 2. Configurar en Keycloak (http://localhost:8080) el realm "banktransfer-realm",
#    el cliente "transfer-api" y el rol "cliente_banca"

# 3. Correr la API en modo dev (puerto 8081)
./mvnw quarkus:dev
```

Las migraciones de Flyway se ejecutan automáticamente al iniciar.

## 🧪 Tests

```bash
./mvnw test
```

Los tests usan `@QuarkusTest` y **no necesitan Docker ni conexión a internet**: Kafka se reemplaza por el conector en memoria de SmallRye, la base de datos por H2 en memoria, la seguridad con `@TestSecurity` y las dependencias con mocks (`@InjectMock`, `PanacheMock`).

## 🗂️ Estructura

```
src/main/java/org/acme/
├── TransferResource.java      # Endpoints REST + caché
├── TransferService.java       # Lógica de negocio + emisión de eventos
├── NotificacionConsumer.java  # Consumidor Kafka
├── Transfer.java              # Entidad JPA (Panache)
├── TransferEvent.java         # Evento (record)
└── User.java                  # Entidad de usuarios
src/main/resources/
├── application.properties
└── db/migration/              # Scripts Flyway
```

## 🛣️ Próximos pasos

- [ ] Validación de saldo y estados de la transferencia (PENDIENTE → COMPLETADA / RECHAZADA)
- [ ] Manejo de errores y Dead Letter Queue en Kafka
- [ ] Documentación OpenAPI / Swagger UI

---

Desarrollado por **[Cristian Encarnación](https://github.com/cristianEncarnacion)** 🇩🇴
