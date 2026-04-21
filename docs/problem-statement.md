# Problem Statement

BrightSmile Advantis Dental Surgeries operates multiple dental surgery locations and serves patients through office-manager assisted booking. Before this system, patient, dentist, surgery, and appointment information could be scattered across spreadsheets, phone notes, and manual calendars. That creates several business problems:

1. Office managers cannot reliably see a complete schedule across all surgeries.
2. Patients may be booked with dentists who are already busy.
3. Patients with outstanding unpaid bills may still request additional services.
4. Appointment confirmation status is hard to track.
5. Managers lack a simple operational dashboard for upcoming appointments and clinic capacity.

The BrightSmile Dental Surgery Appointment Webapp solves these problems with a secure, centralized web application. It records patients, dentists, surgery locations, appointments, user roles, and dental bills in a relational database. It exposes a clean REST API with DTOs, enforces core scheduling rules in the service layer, and provides a browser-based staff console for day-to-day operations.

## Stakeholders

- Office Manager: registers patients, searches patients, schedules appointments, cancels appointments, and monitors the daily schedule.
- Administrator: can perform all Office Manager tasks and manage protected data operations.
- Dentist: relies on the schedule and patient details prepared by office staff.
- Patient: receives correctly scheduled appointments and avoids scheduling issues caused by account/billing status.

## Project Scope

In scope:

- Staff authentication with JWT.
- Patient registration, listing, search, and update API.
- Dentist and surgery directory API.
- Appointment scheduling, listing, cancellation, and status updates.
- Business rules for unpaid bills, dentist weekly load, and double-booked slots.
- Dashboard and web UI for demo-ready workflow.
- Automated tests, Docker deployment, CI workflow, and design artefacts.

Out of scope for this course-project version:

- Real SMTP email delivery.
- Payment processing.
- Public patient self-service portal.
- Calendar integration with external systems.
