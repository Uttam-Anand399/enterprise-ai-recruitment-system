package com.enterprise.recruitment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "resumes")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    @Column(name = "content_type", length = 120)
    private String contentType;

    @Lob
    @Column(name = "parsed_text", columnDefinition = "LONGTEXT")
    private String parsedText;

    @Column(name = "is_primary", nullable = false)
    private boolean primaryResume;

    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private Instant uploadedAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Resume() {
    }

    public Resume(AppUser user, String fileName, String fileUrl, String contentType, String parsedText) {
        this.user = user;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.contentType = contentType;
        this.parsedText = parsedText;
        this.primaryResume = true;
    }

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        uploadedAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getParsedText() {
        return parsedText;
    }
}
