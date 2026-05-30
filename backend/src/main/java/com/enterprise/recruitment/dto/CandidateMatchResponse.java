package com.enterprise.recruitment.dto;

import java.math.BigDecimal;
import java.util.List;

public record CandidateMatchResponse(
        Long applicationId,
        Long resumeId,
        Long jobId,
        String jobTitle,
        BigDecimal score,
        List<String> commonKeywords,
        List<String> missingKeywords
) {
}
