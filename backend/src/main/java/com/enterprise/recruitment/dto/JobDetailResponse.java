package com.enterprise.recruitment.dto;

import com.enterprise.recruitment.entity.EmploymentType;
import com.enterprise.recruitment.entity.JobStatus;
import com.enterprise.recruitment.entity.WorkMode;

public record JobDetailResponse(

        Long id,
        String title,
        String department,
        String location,
        String description,
        String requirements,
        EmploymentType employmentType,
        WorkMode workMode,
        JobStatus status

) {
}