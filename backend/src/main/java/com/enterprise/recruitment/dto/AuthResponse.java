package com.enterprise.recruitment.dto;

import com.enterprise.recruitment.entity.Role;

public record AuthResponse(
        String token,
        String tokenType,
        UserSummary user
) {
    public AuthResponse(String token, UserSummary user) {
        this(token, "Bearer", user);
    }

    public record UserSummary(
            Long id,
            String fullName,
            String email,
            Role role
    ) {
    }
}
