# Demo Script

Target length: 3 minutes for the live demo section.

## Setup

1. Keep the EC2 instance running.
2. Open `http://18.221.0.62:8080`.
3. Sign in with `HainingSong / welcome1`.

For local fallback, run `mvn spring-boot:run` and open `http://localhost:8080`.

## Demo Flow

1. Dashboard
   - Show patient, dentist, surgery, appointment, and unpaid bill counts.
   - Point out upcoming schedule.

2. Patient Search
   - Open Patients.
   - Search `Fairfield` or `Bell`.
   - Explain that this uses the DTO-backed REST endpoint `GET /api/v1/patients?search=...`.

3. Register Patient
   - Open New Patient.
   - Enter patient number `P130`, name `Maria Anderson`, email `maria.anderson@example.com`, DOB `1992-08-14`, and an address.
   - Submit and show the patient appears in the patient list.

4. Schedule Appointment
   - Open Appointments.
   - Select `P130`, dentist `D115`, and surgery `S13`.
   - Pick a future date/time and submit.
   - Show confirmation flag and new row.

5. Billing and Business Rule Demo
   - Open Billing.
   - Create an `UNPAID` bill for a patient.
   - Try scheduling that patient.
   - The system rejects the appointment because the patient has an unpaid bill.
   - Mark the bill `PAID`.
   - Schedule again and show the appointment can now be created.
   - Mention that this is enforced in `AppointmentService`, not only in the UI.

6. Security
   - Sign out.
   - Mention that API endpoints return HTTP 401 without a JWT.

7. Cloud Deployment
   - Mention that the live demo is running on AWS EC2.
   - Docker Compose starts two containers: Spring Boot app and MySQL 8.4.
   - Hibernate creates the MySQL tables automatically and `ClinicSeedService` inserts demo data only when the database is empty.
   - User-created patients, bills, and appointments stay in the MySQL Docker volume across Spring Boot restarts.

## Code Walkthrough Pointers

- Domain entities: `src/main/java/.../domain`
- DTOs: `src/main/java/.../dto`
- Business rules: `AppointmentService`
- Billing workflow: `BillingService`, `BillingController`
- Security: `SecurityConfig`, `JwtAuthenticationFilter`, `JwtService`
- Tests: `DentalSurgeryApiIntegrationTests`
- Seed data: `ClinicSeedService`
- Deployment: `Dockerfile`, `docker-compose.yml`, `docs/aws-deployment.md`
