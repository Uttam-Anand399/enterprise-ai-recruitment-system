package com.enterprise.recruitment.dto;

import com.enterprise.recruitment.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(max = 150) String fullName,
        @NotBlank @Email @Size(max = 190) String email,
        @NotBlank @Size(min = 8, max = 100) String password,
        @NotNull Role role
) {
}
