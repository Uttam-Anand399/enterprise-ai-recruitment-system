package com.enterprise.recruitment.repository;

import com.enterprise.recruitment.entity.JobApplication;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    Optional<JobApplication> findByJobIdAndCandidateId(Long jobId, Long candidateId);

    @EntityGraph(attributePaths = {"candidate", "resume", "job"})
    List<JobApplication> findByJobId(Long jobId);

    @EntityGraph(attributePaths = {"job"})
    List<JobApplication> findByCandidateId(Long candidateId);
}