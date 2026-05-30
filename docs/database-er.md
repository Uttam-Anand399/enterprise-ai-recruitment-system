# Database ER Relationship

```mermaid
erDiagram
    users ||--o{ jobs : posts
    users ||--o{ resumes : uploads
    users ||--o{ applications : submits
    users ||--o{ notifications : receives
    jobs ||--o{ applications : receives
    resumes ||--o{ applications : supports
    applications ||--o| match_scores : has

    users {
        BIGINT id PK
        VARCHAR full_name
        VARCHAR email UK
        VARCHAR password_hash
        ENUM role
        ENUM status
        DATETIME created_at
        DATETIME updated_at
    }

    jobs {
        BIGINT id PK
        BIGINT recruiter_id FK
        VARCHAR title
        VARCHAR department
        VARCHAR location
        ENUM employment_type
        ENUM work_mode
        TEXT description
        TEXT requirements
        ENUM status
        DATETIME opened_at
        DATETIME closed_at
        DATETIME created_at
        DATETIME updated_at
    }

    resumes {
        BIGINT id PK
        BIGINT user_id FK
        VARCHAR file_name
        VARCHAR file_url
        VARCHAR content_type
        LONGTEXT parsed_text
        BOOLEAN is_primary
        DATETIME uploaded_at
        DATETIME updated_at
    }

    applications {
        BIGINT id PK
        BIGINT job_id FK
        BIGINT candidate_id FK
        BIGINT resume_id FK
        ENUM status
        TEXT cover_letter
        DATETIME applied_at
        DATETIME updated_at
    }

    match_scores {
        BIGINT id PK
        BIGINT application_id FK
        DECIMAL score
        VARCHAR model_version
        JSON explanation
        DATETIME calculated_at
    }

    notifications {
        BIGINT id PK
        BIGINT user_id FK
        VARCHAR title
        TEXT message
        ENUM type
        DATETIME read_at
        DATETIME created_at
    }
```

## Relationship Summary

- One recruiter user can post many jobs.
- One candidate user can upload many resumes.
- One candidate user can submit many applications.
- One job can receive many applications.
- One resume can support many applications.
- One application can have one match score.
- One user can receive many notifications.
