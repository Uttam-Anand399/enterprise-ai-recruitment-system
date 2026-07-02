package com.enterprise.recruitment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateJobRequest {

    @NotBlank(message = "Job title is required")
    @Size(max = 180, message = "Title cannot exceed 180 characters")
    private String title;

    @Size(max = 120, message = "Department cannot exceed 120 characters")
    private String department;

    @Size(max = 160, message = "Location cannot exceed 160 characters")
    private String location;

    @NotBlank(message = "Job description is required")
    private String description;

    @NotBlank(message = "Requirements are required")
    private String requirements;

    // ===========================
    // GETTERS
    // ===========================

    public String getTitle() {
        return title;
    }

    public String getDepartment() {
        return department;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getRequirements() {
        return requirements;
    }

    // ===========================
    // SETTERS
    // ===========================

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }
}