package com.enterprise.recruitment.dto;

import java.math.BigDecimal;
import java.util.List;

public record AiMatchResponse(
        BigDecimal score,
        List<String> commonKeywords,
        List<String> missingKeywords
) {
}
