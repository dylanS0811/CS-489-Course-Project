# CS489 Course Project: BrightSmile Dental Surgery Appointment Webapp

BrightSmile ADS is a secure Spring Boot web application for managing dental surgery appointments. It is based on the Advantis Dental Surgeries scenario used throughout CS489 and has been consolidated into a standalone final course project.

## Rubric Coverage

| Requirement | Implementation |
| --- | --- |
| Creativity and originality | Complete branded appointment console with dashboard, patient search, billing, scheduling, cancellation, and business-rule feedback. |
| Enterprise solution design | Layered Spring Boot modular monolith: Web/API, DTO, service, repository, domain, and database layers. |
| Functionality and user experience | Static SPA served by Spring Boot at `/`, backed by JSON REST endpoints. |
| Communication and time management | Project artefacts, demo script, and presentation outline are under `docs/`. |
| Problem statement | `docs/problem-statement.md` |
| Requirements and use cases | `docs/requirements-use-cases.md` |
| Domain model class diagram | `docs/domain-model-class-diagram.md` |
| High-level architecture diagram | `docs/architecture.md` |
| ER diagram | `docs/database-er-diagram.md` |
| Git/GitHub repository | Project is structured for Git with source, docs, tests, CI, and deployment files. |
| CI/CD automation | `.github/workflows/maven-ci.yml` runs `mvn -B clean verify`. |
| DTOs | Request/response records under `src/main/java/.../dto`. |
| Security/user authentication | Spring Security + JWT login at `/api/v1/auth/login`. |
| Unit/integration tests | `src/test/java/...` covers service and API workflows. |
| Deployment/containerization | `Dockerfile` and `docker-compose.yml`. |
| Presentation/demo | `docs/presentation/course-project-presentation.md` and `docs/demo-script.md`. |

## Tech Stack

- Java 21
- Spring Boot 3.3.2
- Spring MVC REST
- Spring Security with JWT
- Spring Data JPA / Hibernate
- H2 for default local demo and tests
- MySQL 8.4 through Docker Compose
- Vanilla HTML/CSS/JavaScript web UI
- Maven, JUnit 5, MockMvc

## Run Locally

```bash
mvn spring-boot:run
```

Open:

```text
http://localhost:8080
```

Demo users:

| Username | Password | Role |
| --- | --- | --- |
| `HainingSong` | `welcome1` | OFFICE_MANAGER |
| `ethan.reed` | `welcome1` | ADMINISTRATOR |

By default the app uses an in-memory H2 database and seeds demo data when the database is empty.

## Run Tests

```bash
mvn test
```

The automated tests verify:

- JWT authentication and protected API access
- Dashboard counts
- Patient registration through DTOs
- Appointment scheduling
- Billing workflow and payment-status updates
- Unpaid-bill scheduling rejection
- Dentist weekly appointment limit

## Run With Docker Compose

```bash
docker compose up --build
```

This starts:

- MySQL on host port `3307`
- BrightSmile ADS on host port `8080`

For AWS deployment options, see `docs/aws-deployment.md`.

## Final Submission

For Sakai submission instructions, see `SUBMISSION.md`.

## API Summary

Authentication:

- `POST /api/v1/auth/login`
- `GET /api/v1/auth/me`

Patients:

- `GET /api/v1/patients`
- `GET /api/v1/patients?search={term}`
- `GET /api/v1/patients/{patientId}`
- `POST /api/v1/patients`
- `PUT /api/v1/patients/{patientId}`
- `DELETE /api/v1/patients/{patientId}`

Scheduling:

- `GET /api/v1/appointments`
- `POST /api/v1/appointments`
- `PATCH /api/v1/appointments/{appointmentId}/status`
- `DELETE /api/v1/appointments/{appointmentId}`

Billing:

- `GET /api/v1/bills`
- `GET /api/v1/bills?status=UNPAID`
- `POST /api/v1/bills`
- `PATCH /api/v1/bills/{billId}/status`

Billing statuses:

- `UNPAID`: outstanding balance; patient cannot book a new appointment.
- `PAID`: settled balance; patient can book normally.
- `VOID`: cancelled/invalid bill created by mistake; not counted as unpaid and does not block booking.

Directory/dashboard:

- `GET /api/v1/dentists`
- `GET /api/v1/surgeries`
- `GET /api/v1/dashboard`

## Important Business Rules

- A patient with an unpaid dental bill cannot book a new appointment.
- A dentist cannot have more than five active appointments in the same week.
- A dentist cannot be double-booked for the same date and time.
- Appointment confirmations are marked as sent when a booking succeeds.
