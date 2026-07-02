package com.enterprise.recruitment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;

@Entity
@Table(
        name = "applications",
        uniqueConstraints = @UniqueConstraint(name = "uk_applications_job_candidate", columnNames = {"job_id", "candidate_id"})
)
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private AppUser candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private ApplicationStatus status = ApplicationStatus.SUBMITTED;

    @Column(name = "cover_letter", columnDefinition = "TEXT")
    private String coverLetter;

    @Column(name = "applied_at", nullable = false, updatable = false)
    private Instant appliedAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected JobApplication() {
    }

    public JobApplication(Job job, AppUser candidate, Resume resume) {
        this.job = job;
        this.candidate = candidate;
        this.resume = resume;
    }

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        appliedAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Job getJob() {
        return job;
    }

    public AppUser getCandidate() {
        return candidate;
    }

    public Resume getResume() {
        return resume;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public Instant getAppliedAt() {
        return appliedAt;
    }

    public void updateResume(Resume resume) {
        this.resume = resume;
    }
    public void updateStatus(ApplicationStatus status) {
        this.status = status;
    }
}
