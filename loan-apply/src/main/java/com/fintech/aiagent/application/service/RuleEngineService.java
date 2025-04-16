package com.fintech.aiagent.application.service;

import com.fintech.aiagent.application.dto.CreditEvaluationRequest;
import com.fintech.aiagent.application.dto.CreditEvaluationResponse;

public interface RuleEngineService {
    CreditEvaluationResponse evaluateCredit(CreditEvaluationRequest request);
}