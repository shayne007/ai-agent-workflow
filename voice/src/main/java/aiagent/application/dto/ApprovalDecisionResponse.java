package aiagent.application.dto;

public class ApprovalDecisionResponse {
    private String applicationId;
    private String decision;
    private boolean requiresReview;
    private String riskLevel;
    private String decisionTime;
    private String message;

    public ApprovalDecisionResponse(String applicationId, String decision, boolean requiresReview, 
                                   String riskLevel, String decisionTime, String message) {
        this.applicationId = applicationId;
        this.decision = decision;
        this.requiresReview = requiresReview;
        this.riskLevel = riskLevel;
        this.decisionTime = decisionTime;
        this.message = message;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getDecision() {
        return decision;
    }

    public boolean isRequiresReview() {
        return requiresReview;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public String getDecisionTime() {
        return decisionTime;
    }

    public String getMessage() {
        return message;
    }
}