package com.enterprise.recruitment.repository;

import com.enterprise.recruitment.entity.MatchScore;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchScoreRepository extends JpaRepository<MatchScore, Long> {

    Optional<MatchScore> findByApplicationId(Long applicationId);
}
