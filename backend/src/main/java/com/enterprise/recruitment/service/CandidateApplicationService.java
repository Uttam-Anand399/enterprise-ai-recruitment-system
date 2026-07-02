package com.enterprise.recruitment.service;

import com.enterprise.recruitment.dto.AiMatchResponse;
import com.enterprise.recruitment.dto.AiParseResumeResponse;
import com.enterprise.recruitment.dto.CandidateMatchResponse;
import com.enterprise.recruitment.dto.CandidateRankingResponse;
import com.enterprise.recruitment.entity.AppUser;
import com.enterprise.recruitment.entity.Job;
import com.enterprise.recruitment.entity.JobApplication;
import com.enterprise.recruitment.entity.MatchScore;
import com.enterprise.recruitment.entity.Resume;
import com.enterprise.recruitment.repository.JobApplicationRepository;
import com.enterprise.recruitment.repository.JobRepository;
import com.enterprise.recruitment.repository.MatchScoreRepository;
import com.enterprise.recruitment.repository.ResumeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.enterprise.recruitment.dto.UpdateApplicationStatusRequest;
import com.enterprise.recruitment.entity.ApplicationStatus;

@Service
public class CandidateApplicationService {

    private static final String MODEL_VERSION = "tfidf-cosine-v1";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final JobRepository jobRepository;
    private final ResumeRepository resumeRepository;
    private final JobApplicationRepository applicationRepository;
    private final MatchScoreRepository matchScoreRepository;
    private final AiServiceClient aiServiceClient;

    public CandidateApplicationService(
            JobRepository jobRepository,
            ResumeRepository resumeRepository,
            JobApplicationRepository applicationRepository,
            MatchScoreRepository matchScoreRepository,
            AiServiceClient aiServiceClient
    ) {
        this.jobRepository = jobRepository;
        this.resumeRepository = resumeRepository;
        this.applicationRepository = applicationRepository;
        this.matchScoreRepository = matchScoreRepository;
        this.aiServiceClient = aiServiceClient;
    }

    @Transactional
    public CandidateMatchResponse uploadResumeAndMatch(AppUser candidate, Long jobId, MultipartFile file) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        byte[] fileBytes = readFile(file);
        AiParseResumeResponse parsedResume = aiServiceClient.parseResume(fileBytes, cleanFilename(file.getOriginalFilename()));

        Resume resume = resumeRepository.save(new Resume(
                candidate,
                cleanFilename(file.getOriginalFilename()),
                "uploaded://" + cleanFilename(file.getOriginalFilename()),
                file.getContentType(),
                parsedResume.text()
        ));

        JobApplication application = applicationRepository.findByJobIdAndCandidateId(jobId, candidate.getId())
                .map(existing -> {
                    existing.updateResume(resume);
                    return existing;
                })
                .orElseGet(() -> new JobApplication(job, candidate, resume));
        JobApplication savedApplication = applicationRepository.save(application);

        String jobText = joinJobText(job);
        AiMatchResponse match = aiServiceClient.matchJob(resume.getParsedText(), jobText);
        MatchScore score = matchScoreRepository.findByApplicationId(savedApplication.getId())
                .orElseGet(() -> new MatchScore(savedApplication, match.score(), MODEL_VERSION, toExplanation(match)));
        score.update(match.score(), MODEL_VERSION, toExplanation(match));
        matchScoreRepository.save(score);

        return new CandidateMatchResponse(
                savedApplication.getId(),
                resume.getId(),
                job.getId(),
                job.getTitle(),
                match.score(),
                match.commonKeywords(),
                match.missingKeywords()
        );
    }

    @Transactional(readOnly = true)
    public List<CandidateRankingResponse> getCandidateRanking(Long jobId) {
        return applicationRepository.findByJobId(jobId).stream()
                .map(application -> {
                    BigDecimal score = matchScoreRepository.findByApplicationId(application.getId())
                            .map(MatchScore::getScore)
                            .orElse(BigDecimal.ZERO);
                    return new CandidateRankingResponse(
                            application.getId(),
                            application.getCandidate().getId(),
                            application.getCandidate().getFullName(),
                            application.getCandidate().getEmail(),
                            application.getStatus(),
                            score,
                            application.getAppliedAt()
                    );
                })
                .sorted(Comparator.comparing(CandidateRankingResponse::score).reversed())
                .toList();
    }
    
    @Transactional
    public void updateApplicationStatus(
            Long applicationId,
            ApplicationStatus status
    ) {

        JobApplication application =
                applicationRepository
                        .findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException("Application not found"));

        application.updateStatus(status);

        applicationRepository.save(application);

    }

    private byte[] readFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Resume file is required");
        }
        String filename = cleanFilename(file.getOriginalFilename()).toLowerCase();
        if (!filename.endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF resumes are supported");
        }

        try {
            return file.getBytes();
        } catch (Exception exception) {
            throw new IllegalArgumentException("Unable to read resume file");
        }
    }

    private String cleanFilename(String filename) {
        if (filename == null || filename.isBlank()) {
            return "resume.pdf";
        }
        return filename.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private String joinJobText(Job job) {
        return String.join(" ", job.getTitle(), job.getDescription(), job.getRequirements() == null ? "" : job.getRequirements());
    }

    private String toExplanation(AiMatchResponse match) {
        try {
            return OBJECT_MAPPER.writeValueAsString(new MatchExplanation(match.commonKeywords(), match.missingKeywords()));
        } catch (JsonProcessingException exception) {
            return "{\"commonKeywords\":[],\"missingKeywords\":[]}";
        }
    }

    private record MatchExplanation(List<String> commonKeywords, List<String> missingKeywords) {
    }
}
