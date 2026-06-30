package com.enterprise.recruitment.repository;

import com.enterprise.recruitment.entity.Job;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByRecruiterEmail(String email);

}