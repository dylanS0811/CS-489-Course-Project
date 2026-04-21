# AWS Deployment Guide: Simplest Cloud Demo

For the course presentation, use the simplest deployment:

```text
AWS EC2 + Docker Compose
```

This runs both services on one EC2 instance:

- Spring Boot web application container
- Real MySQL 8.4 container

No ECR, no App Runner, no RDS. This is enough for a clean teacher demo.

## What Will Happen Automatically

When you run:

```bash
docker compose up --build -d
```

Docker Compose starts MySQL first, then starts the Spring Boot app.

The app connects to MySQL using the environment variables in `docker-compose.yml`:

```yaml
DB_URL: jdbc:mysql://mysql:3306/dental_surgery_db?createDatabaseIfNotExist=true...
DB_DRIVER: com.mysql.cj.jdbc.Driver
DB_USERNAME: root
DB_PASSWORD: 123456
DDL_AUTO: update
SEED_ENABLED: "true"
```

Because `DDL_AUTO=update`, Hibernate creates the database tables automatically.

Because `SEED_ENABLED=true`, the app inserts demo data automatically at startup.

Demo login:

```text
Username: HainingSong
Password: welcome1
```

## EC2 Deployment Steps

### 1. Create EC2 Instance

Recommended for a stable course demo:

```text
AMI: Amazon Linux 2023
Instance type: t3.medium
Security group inbound rules:
  SSH 22
  Custom TCP 8080
```

Port `8080` must be open so the web app can be viewed in a browser.

`t3.micro` can run the app after startup, but it may become slow during Docker/Maven build because the project starts both Java and MySQL. `t3.medium` is the simplest stable choice for a one-day presentation demo.

### 2. SSH Into EC2

```bash
ssh -i your-key.pem ec2-user@<EC2_PUBLIC_IP>
```

### 3. Install Docker and Git

```bash
sudo dnf update -y
sudo dnf install -y docker git
sudo systemctl enable --now docker
sudo usermod -aG docker ec2-user
```

Log out and log back in:

```bash
exit
ssh -i your-key.pem ec2-user@<EC2_PUBLIC_IP>
```

### 4. Clone Project

```bash
git clone <your-github-repo-url>
cd CS-489-Course-Project
```

### 5. Start Application

```bash
docker compose up --build -d
```

Check containers:

```bash
docker ps
```

Expected containers:

```text
cs489-course-project-mysql
cs489-course-project-app
```

### 6. Open the Web App

```text
http://<EC2_PUBLIC_IP>:8080
```

Login:

```text
HainingSong / welcome1
```

## Useful Commands

View logs:

```bash
docker compose logs -f
```

Restart:

```bash
docker compose restart
```

Stop the demo:

```bash
docker compose down
```

Remove MySQL data volume and start fresh:

```bash
docker compose down -v
docker compose up --build -d
```

## Teacher Explanation

You can say:

> For local development, the app can run with H2 for convenience. For cloud demo, I deployed it on AWS EC2 using Docker Compose. The cloud environment runs a real MySQL 8.4 container. Hibernate automatically creates the tables, and the Spring Boot seed service automatically inserts demo users, patients, dentists, surgeries, appointments, and bills.

## One-Day Cost Estimate

For the current EC2 setup in the Ohio region:

```text
t3.medium Linux: about $0.0416/hour, about $1.00/day
Public IPv4: about $0.005/hour, about $0.12/day
20 GB gp3 EBS volume: only a few cents for one day
```

For a one-day teacher demo, the total is roughly $1.10 to $1.50, excluding unusual data transfer. Stop or terminate the instance after submission to avoid continued charges.
