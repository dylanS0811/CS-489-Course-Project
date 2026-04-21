# Database E-R Diagram

PNG deliverable:

![Database E-R Diagram](/Users/hainingsong/IdeaProjects/CS-489-Course-Project/docs/images/database-er-diagram.png)

Source renderer:

```text
scripts/render_project_diagrams.swift
```

Main database tables shown:

- `ROLES`
- `APP_USERS`
- `PATIENTS`
- `ADDRESSES`
- `DENTISTS`
- `SURGERIES`
- `APPOINTMENTS`
- `DENTAL_BILLS`

Important database constraints:

- `patients.patient_number` is unique.
- `patients.email` is unique.
- `dentists.dentist_code` is unique.
- `dentists.email` is unique.
- `surgeries.surgery_number` is unique.
- `appointments` has a unique dentist/date/time slot constraint.
- `dental_bills.appointment_id` is unique so one appointment can generate at most one bill.
