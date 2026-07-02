package com.enterprise.recruitment.dto;

import com.enterprise.recruitment.entity.ApplicationStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record CandidateApplicationResponse(

        Long applicationId,
        Long jobId,
        String jobTitle,
        ApplicationStatus status,
        BigDecimal score,
        Instant appliedAt

) {
}