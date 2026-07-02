package com.enterprise.recruitment.controller;

import com.enterprise.recruitment.dto.CandidateRankingResponse;
import com.enterprise.recruitment.entity.AppUser;
import com.enterprise.recruitment.entity.Role;
import com.enterprise.recruitment.security.RoleGuard;
import com.enterprise.recruitment.service.CandidateApplicationService;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.enterprise.recruitment.dto.UpdateApplicationStatusRequest;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
@RestController
@RequestMapping("/api/recruiter")
public class RecruiterController {

    private final CandidateApplicationService candidateApplicationService;
    private final RoleGuard roleGuard;

    public RecruiterController(CandidateApplicationService candidateApplicationService, RoleGuard roleGuard) {
        this.candidateApplicationService = candidateApplicationService;
        this.roleGuard = roleGuard;
    }

    @GetMapping("/jobs/{jobId}/rankings")
    public List<CandidateRankingResponse> rankings(
            @AuthenticationPrincipal AppUser user,
            @PathVariable Long jobId
    ) {
        roleGuard.requireAnyRole(user, Role.RECRUITER, Role.ADMIN);
        return candidateApplicationService.getCandidateRanking(jobId);
    }
    @PatchMapping("/applications/{applicationId}/status")
    public void updateStatus(

            @AuthenticationPrincipal AppUser user,

            @PathVariable Long applicationId,

            @RequestBody UpdateApplicationStatusRequest request

    ) {

        roleGuard.requireAnyRole(user, Role.RECRUITER, Role.ADMIN);

        candidateApplicationService.updateApplicationStatus(
                applicationId,
                request.getStatus()
        );

    }
}
