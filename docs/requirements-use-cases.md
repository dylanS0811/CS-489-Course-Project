# Requirements and Use Cases

## Functional Requirements

1. The system shall authenticate staff users before allowing access to clinic data.
2. The system shall support role-based authorization for Office Manager and Administrator users.
3. The system shall allow staff to view an operational dashboard.
4. The system shall allow staff to register new patients.
5. The system shall allow staff to search patients by number, name, phone, email, city, state, or ZIP code.
6. The system shall maintain dentist records with code, name, phone, email, and specialization.
7. The system shall maintain surgery records with surgery number, name, phone, and address.
8. The system shall allow staff to schedule a dental appointment for a patient.
9. The system shall record appointment date, time, status, reason, patient, dentist, surgery, booked-by user, and confirmation flag.
10. The system shall allow staff to create dental-service bills for patients.
11. The system shall allow staff to filter bills by payment status and update a bill to paid, unpaid, or void.
12. The system shall prevent a patient with an unpaid bill from booking a new appointment.
13. The system shall prevent a dentist from being double-booked at the same date and time.
14. The system shall prevent a dentist from having more than five active appointments in one week.
15. The system shall allow staff to cancel appointments.
16. The system shall expose DTO-based REST APIs for the frontend and tests.
17. The system shall seed realistic demo data for presentation and local testing.

## Nonfunctional Requirements

1. The backend shall use Java 21 and Spring Boot.
2. The persistence layer shall use Spring Data JPA and a relational database.
3. The application shall be deployable as a Docker container.
4. The project shall include automated tests runnable with Maven.
5. The system shall return structured JSON error responses for validation, authorization, and business-rule failures.
6. The user interface shall be usable on desktop and mobile screen sizes.

## Use Case 1: Login

Primary actor: Office Manager or Administrator

Main flow:

1. User opens the web application.
2. User enters username and password.
3. System validates credentials.
4. System returns a JWT and displays the staff console.

Alternative flow:

- If credentials are invalid, the system returns HTTP 401 and displays an error.

## Use Case 2: Register Patient

Primary actor: Office Manager

Main flow:

1. User opens New Patient.
2. User enters patient number, name, phone, email, date of birth, and address.
3. System validates required fields and email format.
4. System stores the patient and returns the patient DTO.
5. Patient appears in patient search and appointment scheduling lists.

Alternative flow:

- If patient number or email already exists, the system returns HTTP 409.

## Use Case 3: Schedule Appointment

Primary actor: Office Manager

Main flow:

1. User selects patient, dentist, surgery, date, time, and reason.
2. System verifies the patient exists.
3. System verifies the dentist and surgery exist.
4. System checks unpaid bills.
5. System checks dentist weekly appointment count.
6. System checks dentist slot availability.
7. System creates the appointment, marks confirmation as sent, and returns the appointment DTO.

Alternative flows:

- If the patient has an unpaid bill, the system returns HTTP 409.
- If the dentist is already booked at the same slot, the system returns HTTP 409.
- If the dentist already has five active appointments that week, the system returns HTTP 409.

## Use Case 4: Search Patients

Primary actor: Office Manager

Main flow:

1. User enters a search term.
2. System searches patient identity, contact, and address fields.
3. System returns matching patient DTOs sorted by last name and first name.

## Use Case 5: Cancel Appointment

Primary actor: Office Manager

Main flow:

1. User views appointments.
2. User cancels a selected appointment.
3. System changes status to `CANCELLED`.
4. Updated appointment list is displayed.

## Use Case 6: Manage Dental Bills

Primary actor: Office Manager

Main flow:

1. User opens Billing.
2. User selects a patient, optional appointment, issue date, amount, status, and description.
3. System validates the request and stores the dental-service bill.
4. User filters bills by status.
5. User marks an unpaid bill as paid.
6. Dashboard unpaid-bill count and appointment eligibility update after refresh.

Alternative flow:

- If a bill is marked `UNPAID`, the patient cannot schedule a new appointment until the bill is changed to `PAID` or `VOID`.
