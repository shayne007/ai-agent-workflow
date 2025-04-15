package com.fintech.aiagent.interfaces.rest;

import com.fintech.aiagent.application.dto.CreditEvaluationRequest;
import com.fintech.aiagent.application.dto.CreditEvaluationResponse;
import com.fintech.aiagent.application.service.RuleEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/engine")
public class RuleEngineController {

    private final RuleEngineService ruleEngineService;

    @Autowired
    public RuleEngineController(RuleEngineService ruleEngineService) {
        this.ruleEngineService = ruleEngineService;
    }

    @PostMapping("/evaluate")
    public ResponseEntity<CreditEvaluationResponse> evaluateCredit(@RequestBody CreditEvaluationRequest request) {
        CreditEvaluationResponse response = ruleEngineService.evaluateCredit(request);
        return ResponseEntity.ok(response);
    }
}