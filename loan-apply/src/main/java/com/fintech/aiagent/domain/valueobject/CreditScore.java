package com.fintech.aiagent.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonCreator;

public record CreditScore(int score) {
    public CreditScore {
        if (score < 0 || score > 900) {
            throw new IllegalArgumentException("信用分数必须在0-900之间");
        }
    }
    @JsonCreator
    public static CreditScore getInstance(int score) {
        return new CreditScore(score);
    }

    public RiskLevel getRiskLevel() {
        if (score >= 700) {
            return RiskLevel.LOW;
        }
        if (score >= 600) {
            return RiskLevel.MEDIUM;
        }
        return RiskLevel.HIGH;
    }
}