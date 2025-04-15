package com.fintech.aiagent.application.service;

import com.fintech.aiagent.application.dto.ApprovalDecisionRequest;
import com.fintech.aiagent.application.dto.ApprovalDecisionResponse;

public interface ApprovalService {
    ApprovalDecisionResponse makeDecision(ApprovalDecisionRequest request);
}