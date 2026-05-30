package com.enterprise.recruitment.dto;

public class CreateJobRequest {

    private String title;
    private String department;
    private String location;
    private String description;
    private String requirements;

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
}