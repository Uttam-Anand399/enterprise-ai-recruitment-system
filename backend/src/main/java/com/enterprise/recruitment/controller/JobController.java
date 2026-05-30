package com.enterprise.recruitment.controller;

import com.enterprise.recruitment.dto.CreateJobRequest;
import com.enterprise.recruitment.entity.Job;
import com.enterprise.recruitment.service.JobService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService service;

    public JobController(
            JobService service
    ) {
        this.service = service;
    }

//    @PostMapping
//    public Job create(
//            @RequestBody
//            CreateJobRequest request
//    ) {
//
//        return service.create(
//                request
//        );
//
//    }
    @PostMapping
    public Job create(
    @RequestBody
    CreateJobRequest request
    ){

    return service.create(
    request
    );

    }

}