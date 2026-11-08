# LoanPro Microservices

A loan application system built as Spring Cloud microservices. A user registers
and logs in to receive a JWT, submits a loan application with that token, and
the system immediately evaluates it with a simple income rule and stores the
outcome.

## How a loan flows through the system

1. **Register / login** — `auth-service` stores the user (BCrypt-hashed
   password) and returns a signed JWT (HS256, 1-hour expiry by default)
   carrying the user's email and role (`USER` / `ADMIN`).
2. **Apply** — the client sends the JWT to `loan-service`
   (`POST /api/loans`). The service validates the token with the same shared
   secret, rejects a second application for the same email, and asks
   `decision-service` for a decision through an OpenFeign client.
3. **Decision** — `decision-service` approves the loan when the requested
   amount is **at most 50% of the applicant's annual income**, and rejects it
   otherwise. The resulting status (`APPROVED` / `REJECTED`) is saved with the
   application.
4. **Manage** — applications can be listed and fetched by their owner role;
   changing an application's status and deleting applications are restricted
   to `ADMIN` tokens.

All traffic can go through `api-gateway`, which routes by path and
load-balances across instances registered in Eureka.

## Modules

| Module | Port | Responsibility |
|---|---|---|
| `service-registry` | 8761 | Eureka server — service discovery dashboard and registry |
| `config-server` | 8888 | Spring Cloud Config server (native profile — serves the files in `config-server/src/main/resources/config/`) |
| `api-gateway` | 8080 | Spring Cloud Gateway (WebMVC) — routes `/api/auth/**`, `/api/loans/**`, `/api/decisions/**` via `lb://` |
| `auth-service` | 8081 | Registration and login, JWT issuing; MySQL database `loanpro_auth` |
| `loan-service` | 8082 | Loan application CRUD, JWT validation, Feign call to the decision service; MySQL database `loanpro_loans` |
| `decision-service` | 8083 | Stateless eligibility rule evaluation (no database) |

## Tech stack

- Java 17, Spring Boot 4.0.6, Spring Cloud 2025.1.1
- Spring Cloud Netflix Eureka, Spring Cloud Config, Spring Cloud Gateway
  Server WebMVC, Spring Cloud LoadBalancer, OpenFeign
- Spring Security with JJWT 0.11.5 (HS256 tokens)
- Spring Data JPA + MySQL 8 (one database per data-owning service)
- springdoc-openapi Swagger UI in `loan-service`
- Lombok, Maven, Docker / Docker Compose

## Run with Docker Compose

Requires Docker with the Compose plugin:

```bash
docker compose up --build
```

This starts MySQL, the registry, the config server, all three business
services and the gateway, in dependency order. The two MySQL databases
(`loanpro_auth`, `loanpro_loans`) are created automatically on first
connection. Once everything is up:

- Gateway: http://localhost:8080
- Eureka dashboard: http://localhost:8761
- Loan service Swagger UI: http://localhost:8082/swagger-ui/index.html

## Run locally without Docker

Prerequisites: JDK 17+, Maven 3.9+, and a MySQL 8 server running locally
(the services connect as `root`/`root` by default and auto-create their
databases — override with the environment variables below).

Start the modules in this order, one terminal each
(`mvn spring-boot:run` inside the module directory):

1. `service-registry`
2. `config-server`
3. `auth-service`, `loan-service`, `decision-service`
4. `api-gateway`

Every service also carries a complete local `application.properties`, so the
config server import is optional — the system runs the same with or without
it. When the config server is present, it serves each service the settings
committed under `config-server/src/main/resources/config/`.

## Key endpoints

Through the gateway (`http://localhost:8080`) or directly on each service:

| Method & path | Auth | Description |
|---|---|---|
| `POST /api/auth/register` | public | Register (`fullName`, `email`, `password`) — returns a JWT |
| `POST /api/auth/login` | public | Login (`email`, `password`) — returns a JWT |
| `POST /api/loans` | `USER` / `ADMIN` | Submit a loan application — evaluated immediately |
| `GET /api/loans` | `USER` / `ADMIN` | List all applications |
| `GET /api/loans/{id}` | `USER` / `ADMIN` | Get one application |
| `PUT /api/loans/{id}/status?status=APPROVED` | `ADMIN` | Override an application's status |
| `DELETE /api/loans/{id}` | `ADMIN` | Delete an application |
| `POST /api/decisions/evaluate` | — | Decision rule (`amount`, `annualIncome`) — called internally by `loan-service` via Feign |

Example:

```bash
# Register and capture the token
curl -X POST http://localhost:8080/api/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"fullName":"Jane Doe","email":"jane@example.com","password":"secret123"}'

# Apply for a loan (amount 20000 on income 60000 -> APPROVED, within the 50% rule)
curl -X POST http://localhost:8080/api/loans \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer <token>' \
  -d '{"applicantName":"Jane Doe","email":"jane@example.com","loanType":"PERSONAL","amount":20000,"annualIncome":60000,"employmentStatus":"EMPLOYED"}'
```

## Configuration

All services are configured through environment variables with local-dev
defaults:

| Variable | Default | Used by |
|---|---|---|
| `DB_HOST` / `DB_PORT` | `localhost` / `3306` | auth-service, loan-service |
| `DB_USERNAME` / `DB_PASSWORD` | `root` / `root` | auth-service, loan-service |
| `JWT_SECRET` | dev-only placeholder | auth-service (signing), loan-service (validation) — must be identical in both |
| `JWT_EXPIRATION` | `3600000` (1 hour, ms) | auth-service |
| `EUREKA_URL` | `http://localhost:8761/eureka/` | all services |
| `CONFIG_SERVER_URL` | `http://localhost:8888` | all client services |

> The default `JWT_SECRET` is a development placeholder kept for parity with
> the originally hardcoded value. Set a real secret through the environment
> before running anywhere beyond a local machine.
