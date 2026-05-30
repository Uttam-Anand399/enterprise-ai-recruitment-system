package com.enterprise.recruitment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "match_scores")
public class MatchScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private JobApplication application;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal score;

    @Column(name = "model_version", length = 80)
    private String modelVersion;

    @Column(columnDefinition = "JSON")
    private String explanation;

    @Column(name = "calculated_at", nullable = false)
    private Instant calculatedAt;

    protected MatchScore() {
    }

    public MatchScore(JobApplication application, BigDecimal score, String modelVersion, String explanation) {
        this.application = application;
        this.score = score;
        this.modelVersion = modelVersion;
        this.explanation = explanation;
    }

    @PrePersist
    void prePersist() {
        calculatedAt = Instant.now();
    }

    public BigDecimal getScore() {
        return score;
    }

    public String getExplanation() {
        return explanation;
    }

    public Instant getCalculatedAt() {
        return calculatedAt;
    }

    public void update(BigDecimal score, String modelVersion, String explanation) {
        this.score = score;
        this.modelVersion = modelVersion;
        this.explanation = explanation;
        this.calculatedAt = Instant.now();
    }
}
