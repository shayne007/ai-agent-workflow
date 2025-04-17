package com.fintech.aiagent.application.service.impl;

import com.fintech.aiagent.application.dto.ApprovalDecisionRequest;
import com.fintech.aiagent.application.dto.ApprovalDecisionResponse;
import com.fintech.aiagent.application.service.ApprovalService;
import com.fintech.aiagent.domain.aggregate.LoanApplyAggregate;
import com.fintech.aiagent.domain.entity.CreditReport;
import com.fintech.aiagent.domain.repository.CreditReportRepository;
import com.fintech.aiagent.domain.repository.LoanApplyRepository;
import com.fintech.aiagent.domain.valueobject.ApprovalDecision;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApprovalServiceImpl implements ApprovalService {

    @Autowired
    LoanApplyRepository applicationStore;
    @Autowired
    CreditReportRepository creditReportRepository;

    @Override
    public ApprovalDecisionResponse makeDecision(ApprovalDecisionRequest request) {
        // 1.1 获取申请信息
        String applicationId = request.getApplicationId();
        LoanApplyAggregate aggregate = applicationStore.get(applicationId);
        // 1.2 获取征信报告
        CreditReport creditReport = creditReportRepository.get(aggregate.getCustomerId().getId());
        // 1.3 根据征信报告重建申请信息
        LoanApplyAggregate application =
            new LoanApplyAggregate(aggregate.getApplication(), creditReport);

        if (application == null) {
            throw new IllegalArgumentException("未找到申请ID: " + applicationId);
        }

        // 2. 执行自动决策
        ApprovalDecision decision = application.makeAutomaticDecision();

        // 3. 判断是否需要人工审核
        boolean requiresReview = application.requiresHumanReview();

        // 4. 构建响应
        return new ApprovalDecisionResponse(applicationId, decision.getCode(), requiresReview,
            application.getRiskLevel().toString(), Instant.now().toString(),
            requiresReview ? "等待人工审核" : "自动审批完成");
    }
}