# Software Solution Architecture

PNG deliverable:

![Software Solution Architecture](/Users/hainingsong/IdeaProjects/CS-489-Course-Project/docs/images/solution-architecture.png)

Source renderer:

```text
scripts/render_project_diagrams.swift
```

Architecture style:

- 3-tier web architecture
- Layered modular monolith
- Containerized cloud demo deployment

Cloud demo deployment:

- Browser client accesses the app over port `8080`.
- AWS EC2 hosts Docker Compose.
- Spring Boot runs as one container.
- MySQL 8.4 runs as one container.
- Hibernate creates tables automatically.
- Seed service inserts demo data automatically only when the database is empty.

Logical layers:

- Client application
- Security and access control
- Presentation / API layer
- Business service layer
- Persistence layer
- MySQL data layer
