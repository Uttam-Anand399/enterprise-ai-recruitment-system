package com.enterprise.recruitment.dto;

import java.util.List;

public record AiParseResumeResponse(
        String text,
        String email,
        String phone,
        List<String> keywords,
        Integer word_count
) {
}
