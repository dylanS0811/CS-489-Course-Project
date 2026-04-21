# CS489 Course Project Final Submission

Project: BrightSmile Dental Surgery Appointment Webapp

Student/demo user:

```text
Username: HainingSong
Password: welcome1
```

## What To Submit On Sakai

Submit these two URLs in the Sakai text box:

```text
GitHub repository:
<paste-your-github-repository-url-here>

Deployed web application:
http://<EC2_PUBLIC_IP>:8080
```

If the instructor asks for an attachment, attach the generated zip:

```text
submission/CS489-Course-Project-HainingSong.zip
```

## Repository Checklist

The repository includes:

- Source code: `src/`
- Database script: `docs/database/schema.sql`
- Database E-R diagram PNG: `docs/images/database-er-diagram.png`
- Domain model class diagram PNG: `docs/images/domain-model-class-diagram.png`
- Architecture diagram PNG: `docs/images/solution-architecture.png`
- Requirements/use cases: `docs/requirements-use-cases.md`
- Problem statement: `docs/problem-statement.md`
- AWS deployment guide: `docs/aws-deployment.md`
- Presentation deck: `docs/presentation/BrightSmile-ADS-Course-Project.pptx`
- Docker deployment files: `Dockerfile`, `docker-compose.yml`
- CI workflow: `.github/workflows/maven-ci.yml`
- Tests: `src/test/java`

## Package A Zip For Attachment

Run:

```bash
bash scripts/package_submission.sh
```

This runs tests and creates:

```text
submission/CS489-Course-Project-HainingSong.zip
```

## EC2 Demo Summary

On EC2:

```bash
git clone <your-github-repository-url>
cd CS-489-Course-Project
docker compose up --build -d
```

Open:

```text
http://<EC2_PUBLIC_IP>:8080
```

The cloud demo uses a real MySQL 8.4 container. Hibernate creates tables automatically and the seed service inserts demo data automatically.
