package com.enterprise.recruitment.service;

import com.enterprise.recruitment.dto.CreateJobRequest;
import com.enterprise.recruitment.entity.AppUser;
import com.enterprise.recruitment.entity.Job;
import com.enterprise.recruitment.repository.AppUserRepository;
import com.enterprise.recruitment.repository.JobRepository;
import com.enterprise.recruitment.dto.JobSummaryResponse;
import com.enterprise.recruitment.dto.JobDetailResponse;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class JobService {

    private final JobRepository repository;
    private final AppUserRepository userRepository;

    public JobService(
            JobRepository repository,
            AppUserRepository userRepository
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    // ===========================
    // CREATE JOB
    // ===========================
    public Job create(CreateJobRequest request) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();

        System.out.println("EMAIL FROM JWT = " + email);

        AppUser recruiter =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException("Recruiter not found"));

        Job job = new Job();

        job.setRecruiter(recruiter);

        job.setTitle(
                request.getTitle().trim()
        );

        job.setDepartment(
                request.getDepartment()
        );

        job.setLocation(
                request.getLocation()
        );

        job.setDescription(
                request.getDescription()
        );

        job.setRequirements(
                request.getRequirements()
        );

        System.out.println(
                "RECRUITER = "
                        + recruiter.getEmail()
        );

        return repository.save(job);
    }
    public JobDetailResponse update(Long jobId, CreateJobRequest request){

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Job job = repository
                .findByIdAndRecruiterEmail(jobId, email)
                .orElseThrow(() ->
                        new RuntimeException("Job not found or access denied"));

        job.setTitle(request.getTitle().trim());
        job.setDepartment(request.getDepartment());
        job.setLocation(request.getLocation());
        job.setDescription(request.getDescription());
        job.setRequirements(request.getRequirements());

        Job updatedJob = repository.save(job);

        return new JobDetailResponse(
                updatedJob.getId(),
                updatedJob.getTitle(),
                updatedJob.getDepartment(),
                updatedJob.getLocation(),
                updatedJob.getDescription(),
                updatedJob.getRequirements(),
                updatedJob.getEmploymentType(),
                updatedJob.getWorkMode(),
                updatedJob.getStatus()
        );
    }
    
    public void delete(Long jobId) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        Job job = repository
                .findByIdAndRecruiterEmail(jobId, email)
                .orElseThrow(() ->
                        new RuntimeException("Job not found or access denied"));

        repository.delete(job);
    }

    // ===========================
    // GET ALL JOBS
    // ===========================
    public List<JobSummaryResponse> getAllJobs() {

        return repository.findAll()
                .stream()
                .map(job -> new JobSummaryResponse(
                        job.getId(),
                        job.getTitle(),
                        job.getDepartment(),
                        job.getLocation()
                ))
                .toList();

    }
    public List<JobSummaryResponse> getMyJobs() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return repository.findByRecruiterEmail(email)
                .stream()
                .map(job -> new JobSummaryResponse(
                        job.getId(),
                        job.getTitle(),
                        job.getDepartment(),
                        job.getLocation()
                ))
                .toList();

    }
    public JobDetailResponse getJobById(Long id) {

        Job job = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        return new JobDetailResponse(

                job.getId(),
                job.getTitle(),
                job.getDepartment(),
                job.getLocation(),
                job.getDescription(),
                job.getRequirements(),
                job.getEmploymentType(),
                job.getWorkMode(),
                job.getStatus()

        );

    }

}