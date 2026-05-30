CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(190) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('CANDIDATE', 'RECRUITER', 'ADMIN') NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email),
    INDEX idx_users_role (role),
    INDEX idx_users_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS jobs (
    id BIGINT NOT NULL AUTO_INCREMENT,
    recruiter_id BIGINT NOT NULL,
    title VARCHAR(180) NOT NULL,
    department VARCHAR(120),
    location VARCHAR(160),
    employment_type ENUM('FULL_TIME', 'PART_TIME', 'CONTRACT', 'INTERNSHIP', 'TEMPORARY') NOT NULL DEFAULT 'FULL_TIME',
    work_mode ENUM('ONSITE', 'HYBRID', 'REMOTE') NOT NULL DEFAULT 'ONSITE',
    description TEXT NOT NULL,
    requirements TEXT,
    status ENUM('DRAFT', 'OPEN', 'PAUSED', 'CLOSED') NOT NULL DEFAULT 'DRAFT',
    opened_at DATETIME(6),
    closed_at DATETIME(6),
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_jobs_recruiter FOREIGN KEY (recruiter_id) REFERENCES users(id),
    INDEX idx_jobs_recruiter_id (recruiter_id),
    INDEX idx_jobs_status (status),
    INDEX idx_jobs_location (location)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS resumes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_url VARCHAR(500) NOT NULL,
    content_type VARCHAR(120),
    parsed_text LONGTEXT,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    uploaded_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_resumes_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_resumes_user_id (user_id),
    INDEX idx_resumes_is_primary (is_primary)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS applications (
    id BIGINT NOT NULL AUTO_INCREMENT,
    job_id BIGINT NOT NULL,
    candidate_id BIGINT NOT NULL,
    resume_id BIGINT,
    status ENUM('SUBMITTED', 'SCREENING', 'SHORTLISTED', 'INTERVIEW', 'OFFERED', 'HIRED', 'REJECTED', 'WITHDRAWN') NOT NULL DEFAULT 'SUBMITTED',
    cover_letter TEXT,
    applied_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_applications_job FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    CONSTRAINT fk_applications_candidate FOREIGN KEY (candidate_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_applications_resume FOREIGN KEY (resume_id) REFERENCES resumes(id) ON DELETE SET NULL,
    CONSTRAINT uk_applications_job_candidate UNIQUE (job_id, candidate_id),
    INDEX idx_applications_job_id (job_id),
    INDEX idx_applications_candidate_id (candidate_id),
    INDEX idx_applications_status (status),
    INDEX idx_applications_resume_id (resume_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS match_scores (
    id BIGINT NOT NULL AUTO_INCREMENT,
    application_id BIGINT NOT NULL,
    score DECIMAL(5,2) NOT NULL,
    model_version VARCHAR(80),
    explanation JSON,
    calculated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_match_scores_application FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE CASCADE,
    CONSTRAINT uk_match_scores_application UNIQUE (application_id),
    CONSTRAINT chk_match_scores_score CHECK (score >= 0 AND score <= 100),
    INDEX idx_match_scores_score (score),
    INDEX idx_match_scores_calculated_at (calculated_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(180) NOT NULL,
    message TEXT NOT NULL,
    type ENUM('SYSTEM', 'APPLICATION', 'INTERVIEW', 'MATCH', 'SECURITY') NOT NULL DEFAULT 'SYSTEM',
    read_at DATETIME(6),
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_notifications_user_id (user_id),
    INDEX idx_notifications_type (type),
    INDEX idx_notifications_read_at (read_at),
    INDEX idx_notifications_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
