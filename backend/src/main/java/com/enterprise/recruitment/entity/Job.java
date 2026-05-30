package com.enterprise.recruitment.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", nullable = true)
    private AppUser recruiter;

    @Column(nullable = false, length = 180)
    private String title;

    @Column(length = 120)
    private String department;

    @Column(length = 160)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false, length = 40)
    private EmploymentType employmentType = EmploymentType.FULL_TIME;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_mode", nullable = false, length = 40)
    private WorkMode workMode = WorkMode.ONSITE;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private JobStatus status = JobStatus.DRAFT;

    @Column(name = "opened_at")
    private Instant openedAt;

    @Column(name = "closed_at")
    private Instant closedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Job() {
    }

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }

    // ===== GETTERS =====

    public Long getId() {
        return id;
    }

    public AppUser getRecruiter() {
        return recruiter;
    }

    public String getTitle() {
        return title;
    }

    public String getDepartment() {
        return department;
    }

    public String getLocation() {
        return location;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public WorkMode getWorkMode() {
        return workMode;
    }

    public String getDescription() {
        return description;
    }

    public String getRequirements() {
        return requirements;
    }

    public JobStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // ===== SETTERS =====

    public void setRecruiter(AppUser recruiter) {
        this.recruiter = recruiter;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public void setWorkMode(WorkMode workMode) {
        this.workMode = workMode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public void setOpenedAt(Instant openedAt) {
        this.openedAt = openedAt;
    }

    public void setClosedAt(Instant closedAt) {
        this.closedAt = closedAt;
    }
}