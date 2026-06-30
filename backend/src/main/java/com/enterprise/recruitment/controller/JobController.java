package com.enterprise.recruitment.controller;

import com.enterprise.recruitment.dto.CreateJobRequest;
import com.enterprise.recruitment.entity.Job;
import com.enterprise.recruitment.service.JobService;
import com.enterprise.recruitment.dto.JobSummaryResponse;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService service;

    public JobController(JobService service) {
        this.service = service;
    }

    // ===========================
    // CREATE JOB
    // ===========================
    @PostMapping
    public Job create(
            @RequestBody CreateJobRequest request
    ) {
        return service.create(request);
    }

    // ===========================
    // GET ALL JOBS
    // ===========================
    @GetMapping
    public List<JobSummaryResponse> getAllJobs() {
        return service.getAllJobs();
    }
}