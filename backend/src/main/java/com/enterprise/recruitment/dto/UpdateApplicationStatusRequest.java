package com.enterprise.recruitment.dto;

import com.enterprise.recruitment.entity.ApplicationStatus;

public class UpdateApplicationStatusRequest {

    private ApplicationStatus status;

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}