# BrightSmile ADS Course Project Presentation

## Slide 1: Project Title and Cloud Demo

BrightSmile ADS: Dental Surgery Appointment Webapp

- CS489 Course Project by Haining Song.
- Secure appointment operations console for dental surgery staff.
- Cloud demo: `http://18.221.0.62:8080`
- Demo login: `HainingSong / welcome1`
- Runtime stack: Spring Boot + Docker Compose + real MySQL 8.4 on AWS EC2.

## Slide 2: Problem Scenario

- Dental surgeries need one reliable place to manage patients, dentists, surgery locations, appointments, and bills.
- Manual booking can lead to double-booked dentists, unclear confirmation status, and appointments for patients with unpaid balances.
- The solution centralizes scheduling and applies the important rules inside the service layer.

## Slide 3: Requirements and Use Cases

- Staff authentication and role-based access.
- Patient registration, search, update, and removal.
- Billing management: create bills, filter by status, and mark bills paid/unpaid/void.
- Dentist and surgery directory views.
- Appointment scheduling, status updates, and cancellation.
- Business-rule enforcement:
  - Reject booking for patients with unpaid bills.
  - Prevent dentist double-booking at the same date and time.
  - Limit active dentist workload to five appointments per week.

## Slide 4: Domain Model Class Diagram

- Main domain classes: Patient, Dentist, Surgery, Appointment, DentalBill, AppUser, Role, Address.
- Appointment is the central aggregate connecting patient, dentist, surgery, and booking user.
- DentalBill supports the unpaid-balance rule.

## Slide 5: Database E-R Model

- MySQL tables: patients, dentists, surgeries, addresses, appointments, dental_bills, app_users, roles.
- Hibernate creates/updates tables automatically in the cloud when `DDL_AUTO=update`.
- `ClinicSeedService` inserts demo data only when the database is empty, so cloud data persists after restart.
- Important constraints include unique patient numbers, dentist codes, surgery numbers, and dentist appointment slots.

## Slide 6: Solution Architecture

- Browser UI served by Spring Boot.
- REST controllers receive DTO request/response objects.
- Spring Security validates JWT bearer tokens.
- Service layer owns scheduling and billing rules.
- Spring Data JPA/Hibernate persists domain objects to MySQL.
- Docker Compose runs the app container and MySQL container on one EC2 instance.

## Slide 7: Implementation and Technology Stack

- Java 21, Spring Boot 3.3.2, Maven.
- Spring MVC REST API, Spring Data JPA, Hibernate.
- Spring Security + JWT authentication.
- DTO records with validation annotations.
- Billing REST API and UI tab for managing patient payment status.
- Vanilla HTML/CSS/JavaScript single-page UI.
- H2 for default local development/tests; MySQL 8.4 for Docker/AWS deployment.

## Slide 8: Security, DTOs, and Business Rules

- `/api/v1/auth/login` returns a JWT token.
- Protected API endpoints require `Authorization: Bearer <token>`.
- DTOs keep API payloads separate from JPA entities.
- Service methods enforce unpaid-bill rejection, double-booking prevention, and weekly workload limits.
- BillingService lets staff create bills and change payment status, which directly affects appointment eligibility.

## Slide 9: Testing, CI, and AWS Deployment

- Unit test: DTO/entity mapping.
- Integration tests: patient workflow and API workflow.
- MockMvc covers login, dashboard, patient creation, appointment scheduling, and business-rule rejection.
- Billing API test verifies unpaid bill blocks scheduling and paid bill allows scheduling again.
- GitHub Actions runs `mvn -B clean verify`.
- AWS deployment uses EC2 `t3.medium`, Docker Compose, Spring Boot container, and real MySQL 8.4 container.

## Slide 10: Demo, Submission, and Conclusion

- Demo flow:
  - Login as `HainingSong`.
  - Show dashboard counts and upcoming schedule.
  - Search and register patients.
  - Create an unpaid bill and mark it paid.
  - Schedule an appointment.
  - Demonstrate unpaid-bill rejection.
- Submission includes GitHub repository, deployed web app URL, source code, diagrams, tests, Docker files, and presentation deck.
- Future improvements: email delivery, public patient portal, online payments, and calendar integration.
