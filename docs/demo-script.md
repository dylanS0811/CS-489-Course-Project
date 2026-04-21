# Demo Script

Target length: 3 minutes for the live demo section.

## Setup

1. Run `mvn spring-boot:run`.
2. Open `http://localhost:8080`.
3. Sign in with `HainingSong / welcome1`.

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

5. Business Rule Demo
   - Try booking patient `P108`.
   - The system rejects the appointment because P108 has an unpaid bill.
   - Mention that this is enforced in `AppointmentService`, not only in the UI.

6. Security
   - Sign out.
   - Mention that API endpoints return HTTP 401 without a JWT.

## Code Walkthrough Pointers

- Domain entities: `src/main/java/.../domain`
- DTOs: `src/main/java/.../dto`
- Business rules: `AppointmentService`
- Security: `SecurityConfig`, `JwtAuthenticationFilter`, `JwtService`
- Tests: `DentalSurgeryApiIntegrationTests`
