package aiagent.application.dto;

import java.time.Instant;

public class ReviewCaseDTO {
    private String applicationId;
    private String userId;
    private String applicationType;
    private String riskLevel;
    private int creditScore;
    private String aiDecision;
    private Instant submitTime;
    private boolean reviewed;

    public ReviewCaseDTO(String applicationId, String userId, String applicationType, 
                        String riskLevel, int creditScore, String aiDecision, 
                        Instant submitTime, boolean reviewed) {
        this.applicationId = applicationId;
        this.userId = userId;
        this.applicationType = applicationType;
        this.riskLevel = riskLevel;
        this.creditScore = creditScore;
        this.aiDecision = aiDecision;
        this.submitTime = submitTime;
        this.reviewed = reviewed;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getUserId() {
        return userId;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public String getAiDecision() {
        return aiDecision;
    }

    public Instant getSubmitTime() {
        return submitTime;
    }

    public boolean isReviewed() {
        return reviewed;
    }
}