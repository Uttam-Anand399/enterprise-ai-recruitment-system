package com.enterprise.recruitment.repository;

import com.enterprise.recruitment.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
}
