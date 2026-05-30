# Enterprise AI Recruitment Automation Platform

An enterprise recruitment automation monorepo with a React recruiter/candidate UI, Spring Boot API, Flask AI service, and MySQL persistence.

## What It Includes

- React + Vite + Tailwind frontend
- Spring Boot backend with JWT authentication
- MySQL schema for users, jobs, resumes, applications, notifications, and match scores
- Flask AI service for resume parsing and job matching
- Recruiter dashboard with candidate ranking, hiring funnel, analytics, and application status
- Candidate dashboard with resume upload and AI match score flow

## Repository Structure

```text
frontend/     React + Vite + Tailwind application
backend/      Spring Boot API, JWT auth, MySQL persistence, Flask orchestration
ai-service/   Flask service using pdfplumber, spaCy, and scikit-learn
database/     MySQL schema and migrations
docs/         Architecture, API, setup, and environment documentation
```

## Runtime Flow

```text
React frontend
  -> Spring Boot API
      -> MySQL
      -> Flask AI service
          -> resume parsing
          -> job matching
```

## Main Features

- User registration and login with JWT
- Role-aware dashboards for candidates, recruiters, and admins
- Resume PDF upload from candidate dashboard
- Resume parsing via Flask AI service
- Job matching via TF-IDF cosine similarity
- Match score persistence
- Recruiter-side candidate ranking by score
- Hiring funnel and analytics UI

## Documentation

- [Architecture](docs/architecture.md)
- [API Docs](docs/api.md)
- [Setup Guide](docs/setup.md)
- [Environment Variables](docs/environment.md)
- [Database ER Diagram](docs/database-er.md)

## Quick Start

Copy environment files:

```bash
cp frontend/.env.example frontend/.env
cp backend/.env.example backend/.env
cp ai-service/.env.example ai-service/.env
```

Run with Docker Compose:

```bash
docker compose up --build
```

Or run services manually using the [Setup Guide](docs/setup.md).
