package com.enterprise.recruitment.controller;

import com.enterprise.recruitment.dto.CreateJobRequest;
import com.enterprise.recruitment.entity.Job;
import com.enterprise.recruitment.service.JobService;
import com.enterprise.recruitment.dto.JobSummaryResponse;
import com.enterprise.recruitment.dto.JobDetailResponse;
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
    @GetMapping("/my")
    public List<JobSummaryResponse> getMyJobs() {
        return service.getMyJobs();
    }
    @GetMapping("/{id}")
    public JobDetailResponse getJobById(
            @PathVariable Long id
    ) {
        return service.getJobById(id);
    }
 // ===========================
 // UPDATE JOB
 // ===========================
 @PutMapping("/{id}")
 public JobDetailResponse updateJob(
         @PathVariable Long id,
         @RequestBody CreateJobRequest request
 ) {
     return service.update(id, request);
 }
 
 @DeleteMapping("/{id}")
 public void deleteJob(
         @PathVariable Long id
 ) {
     service.delete(id);
 }
}