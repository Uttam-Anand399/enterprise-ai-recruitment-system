package com.enterprise.recruitment.controller;

import com.enterprise.recruitment.dto.CandidateMatchResponse;
import com.enterprise.recruitment.entity.AppUser;
import com.enterprise.recruitment.entity.Role;
import com.enterprise.recruitment.security.RoleGuard;
import com.enterprise.recruitment.service.CandidateApplicationService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import com.enterprise.recruitment.dto.CandidateApplicationResponse;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/candidate")
public class CandidateController {

    private final CandidateApplicationService candidateApplicationService;
    private final RoleGuard roleGuard;

    public CandidateController(CandidateApplicationService candidateApplicationService, RoleGuard roleGuard) {
        this.candidateApplicationService = candidateApplicationService;
        this.roleGuard = roleGuard;
    }

    @PostMapping(value = "/resumes/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CandidateMatchResponse uploadResume(
            @AuthenticationPrincipal AppUser candidate,
            @RequestParam Long jobId,
            @RequestParam("file") MultipartFile file
    ) {
        roleGuard.requireRole(candidate, Role.CANDIDATE);
        return candidateApplicationService.uploadResumeAndMatch(candidate, jobId, file);
    }
    @GetMapping("/applications")
    public List<CandidateApplicationResponse> getMyApplications(
            @AuthenticationPrincipal AppUser candidate
    ) {

        roleGuard.requireRole(candidate, Role.CANDIDATE);

        return candidateApplicationService.getMyApplications(candidate);

    }
}
