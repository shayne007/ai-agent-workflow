package aiagent.application.dto;

import java.time.Instant;

public class ReviewSubmitResponse {
    private String applicationId;
    private String reviewId;
    private String finalDecision;
    private Instant reviewTime;
    private String status;

    public ReviewSubmitResponse(String applicationId, String reviewId, String finalDecision, 
                               Instant reviewTime, String status) {
        this.applicationId = applicationId;
        this.reviewId = reviewId;
        this.finalDecision = finalDecision;
        this.reviewTime = reviewTime;
        this.status = status;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getFinalDecision() {
        return finalDecision;
    }

    public Instant getReviewTime() {
        return reviewTime;
    }

    public String getStatus() {
        return status;
    }
}