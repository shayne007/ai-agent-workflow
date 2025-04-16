package com.fintech.aiagent.domain.credit.entity;

import com.fintech.aiagent.domain.credit.valueobject.ApplicationId;
import com.fintech.aiagent.domain.credit.valueobject.ApprovalDecision;
import com.fintech.aiagent.domain.credit.valueobject.RiskLevel;
import com.fintech.aiagent.domain.customer.valueobject.CustomerId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class CreditApplication {
    private ApplicationId applicationId;
    private CustomerId customerId;
    private String applicationType; // personal_loan, business_loan
    private Instant submitTime;
    private Instant lastUpdateTime;
    private String status; // pending, approved, rejected, under_review
    private List<Document> documents;
    private ApprovalDecision aiDecision;
    private ApprovalDecision finalDecision;
    private RiskLevel riskLevel;
    private String reviewComment;
    private boolean reviewRequired;
    private int creditScore;

    // 构造函数
    public CreditApplication(ApplicationId applicationId, CustomerId customerId, String applicationType) {
        this.applicationId = applicationId;
        this.customerId = customerId;
        this.applicationType = applicationType;
        this.submitTime = Instant.now();
        this.lastUpdateTime = Instant.now();
        this.status = "pending";
        this.documents = new ArrayList<>();
        this.reviewRequired = false;
    }

    // 添加文档
    public void addDocument(Document document) {
        this.documents.add(document);
        this.lastUpdateTime = Instant.now();
    }

    // 设置AI决策结果
    public void setAiDecision(ApprovalDecision decision, RiskLevel riskLevel, int creditScore) {
        this.aiDecision = decision;
        this.riskLevel = riskLevel;
        this.creditScore = creditScore;
        
        // 根据风险等级决定是否需要人工审核
        if (riskLevel == RiskLevel.HIGH || 
            (riskLevel == RiskLevel.MEDIUM && decision == ApprovalDecision.APPROVED)) {
            this.reviewRequired = true;
            this.status = "under_review";
        } else {
            this.finalDecision = decision;
            this.status = decision == ApprovalDecision.APPROVED ? "approved" : "rejected";
        }
        
        this.lastUpdateTime = Instant.now();
    }

    // 人工审核
    public void humanReview(ApprovalDecision decision, String comment) {
        this.finalDecision = decision;
        this.reviewComment = comment;
        this.status = decision == ApprovalDecision.APPROVED ? "approved" : "rejected";
        this.lastUpdateTime = Instant.now();
    }

    // Getters
    public ApplicationId getApplicationId() {
        return applicationId;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public Instant getSubmitTime() {
        return submitTime;
    }

    public Instant getLastUpdateTime() {
        return lastUpdateTime;
    }

    public String getStatus() {
        return status;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public ApprovalDecision getAiDecision() {
        return aiDecision;
    }

    public ApprovalDecision getFinalDecision() {
        return finalDecision;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public boolean isReviewRequired() {
        return reviewRequired;
    }

    public int getCreditScore() {
        return creditScore;
    }
}