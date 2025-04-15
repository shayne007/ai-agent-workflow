package com.fintech.aiagent.application.service.impl;

import com.fintech.aiagent.application.dto.ApprovalDecisionRequest;
import com.fintech.aiagent.application.dto.ApprovalDecisionResponse;
import com.fintech.aiagent.application.service.ApprovalService;
import com.fintech.aiagent.domain.credit.aggregate.CreditApplicationAggregate;
import com.fintech.aiagent.domain.credit.entity.CreditApplication;
import com.fintech.aiagent.domain.credit.valueobject.ApplicationId;
import com.fintech.aiagent.domain.credit.valueobject.ApprovalDecision;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class ApprovalServiceImpl implements ApprovalService {

    // 这里应该注入相关的Repository，用于获取和保存CreditApplication
    // 简化实现，使用内存Map模拟
    private final Map<String, CreditApplicationAggregate> applicationStore = new HashMap<>();

    @Override
    public ApprovalDecisionResponse makeDecision(ApprovalDecisionRequest request) {
        // 1. 获取申请信息
        String applicationId = request.getApplicationId();
        CreditApplicationAggregate application = applicationStore.get(applicationId);
        
        if (application == null) {
            throw new IllegalArgumentException("未找到申请ID: " + applicationId);
        }
        
        // 2. 执行自动决策
        ApprovalDecision decision = application.makeAutomaticDecision();
        
        // 3. 判断是否需要人工审核
        boolean requiresReview = application.requiresHumanReview();
        
        // 4. 构建响应
        return new ApprovalDecisionResponse(
                applicationId,
                decision.getCode(),
                requiresReview,
                application.getRiskLevel().toString(),
                Instant.now().toString(),
                requiresReview ? "等待人工审核" : "自动审批完成"
        );
    }
}