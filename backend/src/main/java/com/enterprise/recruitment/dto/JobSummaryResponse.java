package com.enterprise.recruitment.dto;

public record JobSummaryResponse(
        Long id,
        String title,
        String department,
        String location
) {
}
