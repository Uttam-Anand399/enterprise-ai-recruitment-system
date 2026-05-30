package com.enterprise.recruitment.dto;

import com.enterprise.recruitment.entity.ApplicationStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record CandidateRankingResponse(
        Long applicationId,
        Long candidateId,
        String candidateName,
        String candidateEmail,
        ApplicationStatus status,
        BigDecimal score,
        Instant appliedAt
) {
}
