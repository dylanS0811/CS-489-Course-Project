# Narrative Plan

Audience: CS489 instructor and classmates evaluating the final course project.

Objective: Explain why the BrightSmile Dental Surgery Appointment Webapp is useful, show that it satisfies the project rubric, and prepare a short cloud demo path that fits within the presentation time limit.

Narrative arc:

1. Start with the project identity and business scenario.
2. Show the operational pain points.
3. Translate the scenario into requirements and use cases.
4. Present the domain model, architecture, and database design.
5. Highlight security, DTOs, testing, CI, MySQL persistence, and AWS deployment.
6. Close with a practical demo path, submission package, and future improvements.

Slide list:

1. Project title, value proposition, and cloud demo URL.
2. Problem scenario.
3. Requirements and use cases.
4. Domain model class diagram.
5. Database E-R model and MySQL table creation.
6. Solution architecture.
7. Implementation and technology stack.
8. Security, DTOs, and business rules.
9. Testing, CI, and AWS deployment.
10. Demo path, submission, and conclusion.

Source plan:

- Source code under `src/main/java`.
- Tests under `src/test/java`.
- Project artefacts under `docs`.
- Runtime/deployment files: `Dockerfile`, `docker-compose.yml`, `.github/workflows/maven-ci.yml`.
- Cloud runtime: AWS EC2 `t3.medium`, Docker Compose, Spring Boot app container, MySQL 8.4 container.
- Demo data source: `ClinicSeedService` inserts users, patients, dentists, surgeries, appointments, and bills when `SEED_ENABLED=true` and the database is empty.

Visual system:

- Clean healthcare-operations dashboard look.
- Deep navy, teal, coral, gold, green, and light clinical background.
- Editable PowerPoint text, cards, diagrams, and tables.
- No rasterized slide text.

Editability plan:

- All titles, bullets, labels, metrics, architecture boxes, and diagram labels are editable slide objects.
- Speaker-facing notes remain in Markdown docs and demo script.
