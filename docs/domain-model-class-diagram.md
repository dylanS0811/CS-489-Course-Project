# Domain Model Class Diagram

PNG deliverable:

![Domain Model Class Diagram](/Users/hainingsong/IdeaProjects/CS-489-Course-Project/docs/images/domain-model-class-diagram.png)

Source renderer:

```text
scripts/render_project_diagrams.swift
```

Main domain classes shown:

- `Person`
- `AppUser`
- `Role`
- `Patient`
- `Dentist`
- `Surgery`
- `Address`
- `Appointment`
- `DentalBill`
- `AppointmentStatus`
- `BillStatus`

This model shows the key static relationships used by the application: staff users book appointments, patients attend appointments and owe bills, dentists treat appointments, surgeries host appointments, and addresses support both patient mailing addresses and surgery locations.
