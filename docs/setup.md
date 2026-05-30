# Setup Guide

## Prerequisites

Install:

- Node.js 22+
- Java 21
- Maven 3.9+
- Python 3.12 recommended
- MySQL 8+
- Docker and Docker Compose, optional but recommended

## Environment Files

Create local environment files from examples:

```bash
cp frontend/.env.example frontend/.env
cp backend/.env.example backend/.env
cp ai-service/.env.example ai-service/.env
```

On Windows PowerShell:

```powershell
Copy-Item frontend/.env.example frontend/.env
Copy-Item backend/.env.example backend/.env
Copy-Item ai-service/.env.example ai-service/.env
```

## Option 1: Docker Compose

From the repository root:

```bash
docker compose up --build
```

Expected local URLs:

- Frontend: `http://localhost:5173`
- Backend: `http://localhost:8080`
- AI service: `http://localhost:5000`
- MySQL: `localhost:3306`

## Option 2: Manual Setup

### 1. Database

Create the MySQL database:

```sql
CREATE DATABASE recruitment_platform;
CREATE USER 'recruitment_user'@'%' IDENTIFIED BY 'recruitment_password';
GRANT ALL PRIVILEGES ON recruitment_platform.* TO 'recruitment_user'@'%';
FLUSH PRIVILEGES;
```

Apply schema:

```bash
mysql -u recruitment_user -p recruitment_platform < database/schema.sql
```

### 2. Backend

From `backend/`:

```bash
mvn clean test
mvn spring-boot:run
```

If port `8080` is already in use, run on another port:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

Update `frontend/.env` accordingly:

```text
VITE_API_BASE_URL=http://localhost:8081/api
```

### 3. AI Service

From `ai-service/`:

```bash
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
gunicorn --bind 0.0.0.0:5000 "app.main:create_app()"
```

On Windows PowerShell:

```powershell
py -3.12 -m venv .venv
.\.venv\Scripts\Activate.ps1
pip install -r requirements.txt
python -m flask --app "app.main:create_app" run --host 0.0.0.0 --port 5000
```

Optional spaCy model:

```bash
python -m spacy download en_core_web_sm
```

The service falls back to a blank English spaCy pipeline if the model is not installed.

### 4. Frontend

From `frontend/`:

```bash
npm install
npm run dev
```

Open:

```text
http://localhost:5173
```

## Test and Verification

Frontend:

```bash
cd frontend
npm run lint
npm run build
```

Backend:

```bash
cd backend
mvn test
```

AI service syntax check:

```bash
cd ai-service
python -m compileall app
```

Health checks:

```bash
curl http://localhost:8080/actuator/health
curl http://localhost:5000/health
```

## Seed Data Requirement

Resume matching requires at least one job in the `jobs` table. Insert a recruiter user and job before testing candidate resume upload, or add an admin/job-management feature later.

## Known Local Port Caveat

If another service already occupies `8080`, Spring Boot will not be reachable there. Use a different backend port and update `VITE_API_BASE_URL`.
