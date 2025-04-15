

package com.fintech.aiagent.domain.credit.aggregate;

import com.fintech.aiagent.domain.credit.entity.CreditApplication;
import com.fintech.aiagent.domain.credit.entity.CreditReport;
import com.fintech.aiagent.domain.credit.entity.Document;
import com.fintech.aiagent.domain.credit.valueobject.ApplicationId;
import com.fintech.aiagent.domain.credit.valueobject.ApprovalDecision;
import com.fintech.aiagent.domain.credit.valueobject.CreditScore;
import com.fintech.aiagent.domain.credit.valueobject.RiskLevel;
import com.fintech.aiagent.domain.customer.valueobject.CustomerId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 信贷申请聚合根
 * 负责协调信贷申请流程中的各个实体和值对象
 */
public class CreditApplicationAggregate {
    
    private final CreditApplication application;
    private CreditReport creditReport;
    private final List<String> applicationEvents;
    
    /**
     * 创建新的信贷申请
     */
    public CreditApplicationAggregate(CustomerId customerId, String applicationType) {
        String applicationId = UUID.randomUUID().toString();
        this.application = new CreditApplication(
                new ApplicationId(applicationId),
                customerId,
                applicationType
        );
        this.applicationEvents = new ArrayList<>();
        this.applicationEvents.add("申请创建于 " + Instant.now());
    }
    
    /**
     * 从现有申请重建聚合根
     */
    public CreditApplicationAggregate(CreditApplication application, CreditReport creditReport) {
        this.application = application;
        this.creditReport = creditReport;
        this.applicationEvents = new ArrayList<>();
    }
    
    /**
     * 上传申请材料
     */
    public void uploadDocument(Document document) {
        application.addDocument(document);
        applicationEvents.add("文档上传: " + document.getDocumentType() + " 于 " + Instant.now());
    }
    
    /**
     * 设置信用报告
     */
    public void setCreditReport(CreditReport creditReport) {
        this.creditReport = creditReport;
        applicationEvents.add("信用报告获取于 " + Instant.now() + ", 分数: " + creditReport.getCreditScore().score());
    }
    
    /**
     * 执行自动审批决策
     */
    public ApprovalDecision makeAutomaticDecision() {
        if (creditReport == null) {
            throw new IllegalStateException("无法在没有信用报告的情况下做出决策");
        }
        
        // 获取信用分数和风险等级
        CreditScore creditScore = creditReport.getCreditScore();
        RiskLevel riskLevel = creditScore.getRiskLevel();
        
        // 基于信用分数和风险等级做出决策
        ApprovalDecision decision;
        if (riskLevel == RiskLevel.LOW) {
            decision = ApprovalDecision.APPROVED;
        } else if (riskLevel == RiskLevel.HIGH) {
            decision = ApprovalDecision.REJECTED;
        } else {
            // 中等风险需要进一步审核
            decision = ApprovalDecision.PENDING_REVIEW;
        }
        
        // 更新申请状态
        application.setAiDecision(decision, riskLevel, creditScore.score());
        applicationEvents.add("AI决策: " + decision + " 于 " + Instant.now());
        
        return decision;
    }
    
    /**
     * 执行人工审核
     */
    public void performHumanReview(ApprovalDecision decision, String comment) {
        if (!application.isReviewRequired()) {
            throw new IllegalStateException("此申请不需要人工审核");
        }
        
        application.humanReview(decision, comment);
        applicationEvents.add("人工审核: " + decision + " 于 " + Instant.now() + ", 评论: " + comment);
    }
    
    /**
     * 检查是否需要人工审核
     */
    public boolean requiresHumanReview() {
        return application.isReviewRequired();
    }
    
    /**
     * 获取申请状态
     */
    public String getApplicationStatus() {
        return application.getStatus();
    }
    
    /**
     * 获取申请ID
     */
    public ApplicationId getApplicationId() {
        return application.getApplicationId();
    }
    
    /**
     * 获取客户ID
     */
    public CustomerId getCustomerId() {
        return application.getCustomerId();
    }
    
    /**
     * 获取申请类型
     */
    public String getApplicationType() {
        return application.getApplicationType();
    }
    
    /**
     * 获取申请文档
     */
    public List<Document> getDocuments() {
        return application.getDocuments();
    }
    
    /**
     * 获取AI决策
     */
    public ApprovalDecision getAiDecision() {
        return application.getAiDecision();
    }
    
    /**
     * 获取最终决策
     */
    public ApprovalDecision getFinalDecision() {
        return application.getFinalDecision();
    }
    
    /**
     * 获取风险等级
     */
    public RiskLevel getRiskLevel() {
        return application.getRiskLevel();
    }
    
    /**
     * 获取信用报告
     */
    public CreditReport getCreditReport() {
        return creditReport;
    }
    
    /**
     * 获取申请事件历史
     */
    public List<String> getApplicationEvents() {
        return new ArrayList<>(applicationEvents);
    }
    
    /**
     * 获取原始申请实体
     */
    public CreditApplication getApplication() {
        return application;
    }
}