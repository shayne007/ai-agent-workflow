package com.fintech.aiagent.interfaces.rest;

import com.fintech.aiagent.application.dto.ApprovalDecisionRequest;
import com.fintech.aiagent.application.dto.ApprovalDecisionResponse;
import com.fintech.aiagent.application.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/approval")
public class ApprovalController {

    private final ApprovalService approvalService;

    @Autowired
    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @PostMapping("/decide")
    public ResponseEntity<ApprovalDecisionResponse> makeDecision(@RequestBody ApprovalDecisionRequest request) {
        ApprovalDecisionResponse response = approvalService.makeDecision(request);
        return ResponseEntity.ok(response);
    }
}