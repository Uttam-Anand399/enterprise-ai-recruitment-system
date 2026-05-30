# Backend

Spring Boot authentication service for the Enterprise AI Recruitment Automation Platform.

## Implemented Scope

Authentication:

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/me`

Resume matching:

- `GET /api/jobs`
- `POST /api/candidate/resumes/upload`
- `GET /api/recruiter/jobs/{jobId}/rankings`

## Packages

```text
controller/   Auth REST endpoints and API exception handling
dto/          Auth request and response contracts
entity/       User and role persistence model
repository/   Spring Data JPA repository
security/     JWT filter, token service, and security configuration
service/      Auth orchestration, AI service client, resume matching workflow
```

## Configuration

Environment variables are documented in `.env.example`. JWT signing requires a strong secret of at least 256 bits.

`AI_SERVICE_BASE_URL` points Spring Boot to the Flask service. The default local value is `http://localhost:5000`.
