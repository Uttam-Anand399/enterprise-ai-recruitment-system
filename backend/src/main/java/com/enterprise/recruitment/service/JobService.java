package com.enterprise.recruitment.service;

import com.enterprise.recruitment.dto.CreateJobRequest;
import com.enterprise.recruitment.entity.AppUser;
import com.enterprise.recruitment.entity.Job;
import com.enterprise.recruitment.repository.AppUserRepository;
import com.enterprise.recruitment.repository.JobRepository;

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
}