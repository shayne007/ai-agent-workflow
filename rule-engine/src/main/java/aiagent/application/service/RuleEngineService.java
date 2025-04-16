package aiagent.application.service;

import aiagent.application.dto.CreditEvaluationRequest;
import aiagent.application.dto.CreditEvaluationResponse;

public interface RuleEngineService {
    CreditEvaluationResponse evaluateCredit(CreditEvaluationRequest request);
}