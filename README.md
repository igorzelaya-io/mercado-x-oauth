# MercadoX OAuth Service

`mercado-x-oauth` is the identity provider for the MercadoX ecosystem. It handles user authentication, issues RSA-signed JWTs, and enforces multi-tenant access control.

---

## Prerequisites

- Java 17
- Maven 3.8+
- Docker and Docker Compose
- Access to the GitHub Packages registry for `hn.shadowcore` internal libraries

---

## Quick Start

### 1. Clone and configure environment

Copy the example env file and fill in values:

```bash
cp .env.example .env
```

| Variable | Default | Description |
|---|---|---|
| `DB_USERNAME` | `postgres` | PostgreSQL username |
| `DB_PASSWORD` | `postgres` | PostgreSQL password |
| `JWT_PRIVATE_KEY_LOCATION` | `file:./secrets/private.pem` | Path to RSA private key |
| `JWT_PUBLIC_KEY_LOCATION` | `file:./secrets/public.pem` | Path to RSA public key |
| `JWT_EXPIRATION` | `1h` | Token lifetime (e.g. `30m`, `2h`) |
| `JWT_KEY_ID` | `rsa-1` | Key identifier embedded in JWT `kid` header — increment when rotating keys |

### 2. Generate RSA key pair

The service signs JWTs with an RSA-2048 private key. Keys are stored in the `secrets/` directory (gitignored). Generate them once:

```bash
mkdir -p secrets

# Private key (PKCS#8 format)
openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -out secrets/private.pem

# Public key
openssl rsa -pubout -in secrets/private.pem -out secrets/public.pem
```

### 3. Start infrastructure

```bash
docker-compose up -d
```

This starts PostgreSQL (5432), Redis (6379), Kafka (9092), and Schema Registry (8082).

> **Note:** The oauth service runs on port **8081**. Schema Registry is mapped to host port **8082** in this compose file to avoid the conflict.

### 4. Initialize the database schema

The schema is managed by `mercado-x-library-jpa`. Run it once against the PostgreSQL instance before the first boot:

```bash
# Requires the schema.sql from the mercado-x-library-jpa module
docker exec -i mercadox-postgres psql -U postgres -d mercado_x < /path/to/mercado-x-library-jpa/src/main/resources/schema.sql
```

If you only have this repository, obtain `schema.sql` from the published `mercado-x-library-jpa` JAR:

```bash
jar xf ~/.m2/repository/hn/shadowcore/mercado-x-library-jpa/1.0.0-SNAPSHOT/mercado-x-library-jpa-1.0.0-SNAPSHOT.jar schema.sql
docker exec -i mercadox-postgres psql -U postgres -d mercado_x < schema.sql
```

### 5. Run the service

```bash
mvn spring-boot:run
```

The service starts on `http://localhost:8081`.

---

## Endpoints

| Method | Path | Auth | Description |
|---|---|---|---|
| `POST` | `/api/v1/auth/login` | None | Authenticate and receive a JWT |
| `POST` | `/api/v1/auth/validate` | None | Validate an email verification token |
| `POST` | `/api/v1/register` | None | Register a new user |

### Login example

```bash
curl -X POST http://localhost:8081/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "user@example.com", "password": "secret"}'
```

Response:

```json
{
  "message": "User authenticated successfully",
  "data": "<jwt-token>"
}
```

The JWT includes the following claims:

| Claim | Value |
|---|---|
| `sub` | User email |
| `orgId` | Organization UUID |
| `roles` | List of role names |
| `iss` | `mercadox-oauth` |
| `jti` | Unique token ID (replay protection) |
| `kid` | Key ID header (key rotation support) |

---

## Configuration Reference

All properties can be overridden via environment variables. The service reads `application.yml` with the following defaults:

```yaml
server:
  port: 8081

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/mercado_x
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}
  jpa:
    hibernate:
      ddl-auto: validate        # never modifies schema — requires schema.sql to be run first

security:
  jwt:
    private-key-location: ${JWT_PRIVATE_KEY_LOCATION:file:./secrets/private.pem}
    public-key-location:  ${JWT_PUBLIC_KEY_LOCATION:file:./secrets/public.pem}
    expiration:           ${JWT_EXPIRATION:1h}
    key-id:               ${JWT_KEY_ID:rsa-1}
```

---

## Key Rotation

When rotating the RSA key pair:

1. Generate a new key pair in `secrets/`.
2. Increment `JWT_KEY_ID` (e.g. `rsa-1` → `rsa-2`).
3. Distribute the new public key to all downstream resource servers before restarting this service.
4. Restart the service.

The `kid` header in issued JWTs lets verifiers match tokens to the correct public key during the transition window.

---

## Running Tests

Tests use an H2 in-memory database. The full schema is loaded automatically from `mercado-x-library-jpa` (via `H2JpaTestConfig`) — no local schema file is needed.

```bash
mvn test
```

To run a single test class:

```bash
mvn test -Dtest=MercadoXOauthApplicationTests
```

---

## Internal Dependencies

| Module | Purpose |
|---|---|
| `mercado-x-library-jpa` | JPA entities, repositories, H2 test config, master schema |
| `mercado-x-context` | JWT verification filter chain, `JwtVerifier`, `TenantValidatorFilter` |
| `mercado-x-redis` | Redis client configuration |

All internal dependencies are published to GitHub Packages (`https://maven.pkg.github.com/igorzelaya-io`). Configure Maven with a `~/.m2/settings.xml` that includes a GitHub token with `read:packages` scope to resolve them.

---

## Architecture Role

This service acts as the **identity provider** for the MercadoX ecosystem. It is the only service that holds the RSA private key. All other services receive the public key and use it to verify tokens independently — no token introspection calls are needed at request time.
