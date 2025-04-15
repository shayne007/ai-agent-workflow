package com.fintech.aiagent.application.dto;

import java.util.List;
import java.util.Map;

public class CreditEvaluationResponse {
    private String decision;
    private int score;
    private List<String> riskFactors;
    private List<Map<String, String>> ruleLogs;

    public CreditEvaluationResponse(String decision, int score, List<String> riskFactors, List<Map<String, String>> ruleLogs) {
        this.decision = decision;
        this.score = score;
        this.riskFactors = riskFactors;
        this.ruleLogs = ruleLogs;
    }

    public String getDecision() {
        return decision;
    }

    public int getScore() {
        return score;
    }

    public List<String> getRiskFactors() {
        return riskFactors;
    }

    public List<Map<String, String>> getRuleLogs() {
        return ruleLogs;
    }
}