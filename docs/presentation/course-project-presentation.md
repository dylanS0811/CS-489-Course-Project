# BrightSmile Dental Surgery Appointment Webapp Presentation

## Slide 1: Project Title

BrightSmile Dental Surgery Appointment Webapp

- CS489 Course Project
- Secure web application for dental surgery appointment operations
- Built with Spring Boot, JPA, Spring Security, JWT, REST, and a browser UI

## Slide 2: Problem Scenario

- Dental surgery offices need one reliable place to manage patients, dentists, surgery locations, and appointments.
- Manual booking can lead to double-booked dentists, unclear confirmation status, and appointments for patients with unpaid bills.
- The solution centralizes scheduling and enforces core business rules.

## Slide 3: Requirements and Use Cases

- Staff login and role-based access.
- Register/search patients.
- View dentists and surgeries.
- Schedule and cancel appointments.
- Prevent unpaid-bill booking.
- Prevent dentist double-booking and weekly overload.

## Slide 4: Domain Model

- Main classes: Patient, Dentist, Surgery, Appointment, DentalBill, AppUser, Role, Address.
- Relationships:
  - Patient attends appointments and owes bills.
  - Dentist serves appointments.
  - Surgery hosts appointments.
  - AppUser books appointments.
  - Role authorizes users.

## Slide 5: Architecture

- Browser UI served by Spring Boot.
- REST controllers receive JSON DTOs.
- Spring Security validates JWTs.
- Service layer enforces scheduling rules.
- Spring Data JPA persists to H2 or MySQL.
- Docker Compose runs app plus MySQL.

## Slide 6: Database Design

- Relational tables: patients, dentists, surgeries, addresses, appointments, dental_bills, app_users, roles.
- Unique constraints:
  - Patient number and email.
  - Dentist code and email.
  - Surgery number.
  - Dentist/date/time appointment slot.

## Slide 7: Security and DTOs

- Login endpoint returns JWT token.
- Protected endpoints require Bearer token.
- Role annotations protect operations.
- DTO records separate API payloads from JPA entities.
- Validation annotations protect incoming data.

## Slide 8: Testing and CI

- Maven test suite covers service and API workflows.
- MockMvc verifies login, dashboard, patient creation, appointment scheduling, and business-rule failures.
- GitHub Actions workflow runs `mvn -B clean verify`.

## Slide 9: Demo

- Sign in.
- View dashboard.
- Search patients.
- Register a patient.
- Schedule an appointment.
- Trigger unpaid-bill rejection.
- Show tests passing.

## Slide 10: Conclusion

- The project meets the required enterprise webapp features.
- It includes implementation, tests, deployment, CI, and project artefacts.
- Future improvements: real email delivery, public patient portal, online payments, and external calendar integration.
