# API Documentation

Base URL for local backend:

```text
http://localhost:8080/api
```

Protected endpoints require:

```http
Authorization: Bearer <jwt>
```

## Authentication

### Register

```http
POST /api/auth/register
Content-Type: application/json
```

Request:

```json
{
  "fullName": "Alex Morgan",
  "email": "alex@example.com",
  "password": "securePassword123",
  "role": "CANDIDATE"
}
```

Roles:

- `CANDIDATE`
- `RECRUITER`
- `ADMIN`

Response:

```json
{
  "token": "jwt-token",
  "tokenType": "Bearer",
  "user": {
    "id": 1,
    "fullName": "Alex Morgan",
    "email": "alex@example.com",
    "role": "CANDIDATE"
  }
}
```

### Login

```http
POST /api/auth/login
Content-Type: application/json
```

Request:

```json
{
  "email": "alex@example.com",
  "password": "securePassword123"
}
```

Response shape matches registration.

### Current User

```http
GET /api/auth/me
Authorization: Bearer <jwt>
```

Response:

```json
{
  "id": 1,
  "fullName": "Alex Morgan",
  "email": "alex@example.com",
  "role": "CANDIDATE"
}
```

## Jobs

### List Jobs

```http
GET /api/jobs
Authorization: Bearer <jwt>
```

Response:

```json
[
  {
    "id": 10,
    "title": "Senior Java Developer",
    "department": "Engineering",
    "location": "Bengaluru"
  }
]
```

## Candidate

### Upload Resume and Match Job

```http
POST /api/candidate/resumes/upload
Authorization: Bearer <candidate-jwt>
Content-Type: multipart/form-data
```

Form fields:

- `jobId`: job id to match against
- `file`: PDF resume

Response:

```json
{
  "applicationId": 100,
  "resumeId": 200,
  "jobId": 10,
  "jobTitle": "Senior Java Developer",
  "score": 86.42,
  "commonKeywords": ["java", "spring", "mysql"],
  "missingKeywords": ["kubernetes", "aws"]
}
```

## Recruiter

### Candidate Ranking

```http
GET /api/recruiter/jobs/{jobId}/rankings
Authorization: Bearer <recruiter-or-admin-jwt>
```

Response:

```json
[
  {
    "applicationId": 100,
    "candidateId": 1,
    "candidateName": "Alex Morgan",
    "candidateEmail": "alex@example.com",
    "status": "SHORTLISTED",
    "score": 86.42,
    "appliedAt": "2026-05-21T09:30:00Z"
  }
]
```

## AI Service

Base URL for local AI service:

```text
http://localhost:5000
```

Spring Boot calls these endpoints internally.

### Parse Resume

```http
POST /parse-resume
Content-Type: multipart/form-data
```

Form fields:

- `file`: PDF resume

Response:

```json
{
  "text": "Extracted resume text",
  "email": "alex@example.com",
  "phone": "+91 90000 00000",
  "keywords": ["java", "spring", "mysql"],
  "word_count": 420
}
```

### Match Job

```http
POST /match-job
Content-Type: application/json
```

Request:

```json
{
  "resumeText": "Candidate resume text",
  "jobDescription": "Job description text"
}
```

Response:

```json
{
  "score": 86.42,
  "commonKeywords": ["java", "spring", "mysql"],
  "missingKeywords": ["kubernetes", "aws"]
}
```

## Error Response

Backend errors use this general shape:

```json
{
  "timestamp": "2026-05-21T09:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed"
}
```
