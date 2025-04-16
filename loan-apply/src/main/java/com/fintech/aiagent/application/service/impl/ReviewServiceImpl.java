package com.fintech.aiagent.application.service.impl;

import com.fintech.aiagent.application.dto.ReviewCaseDTO;
import com.fintech.aiagent.application.dto.ReviewSubmitRequest;
import com.fintech.aiagent.application.dto.ReviewSubmitResponse;
import com.fintech.aiagent.application.service.ReviewService;
import com.fintech.aiagent.domain.credit.aggregate.CreditApplicationAggregate;
import com.fintech.aiagent.domain.credit.valueobject.ApprovalDecision;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ReviewServiceImpl implements ReviewService {

    // 模拟存储待审核的申请
    private final Map<String, CreditApplicationAggregate> pendingReviews = new HashMap<>();
    private final Map<String, ReviewCaseDTO> reviewCases = new HashMap<>();

    public ReviewServiceImpl() {
        // 初始化一些模拟数据
        initializeMockData();
    }

    @Override
    public List<ReviewCaseDTO> getReviewCases(String riskLevel, int limit) {
        // 过滤风险等级（如果指定）
        Stream<ReviewCaseDTO> stream = reviewCases.values().stream();
        
        if (riskLevel != null && !riskLevel.isEmpty()) {
            stream = stream.filter(c -> riskLevel.equals(c.getRiskLevel()));
        }
        
        // 按提交时间排序，最新的排在前面
        return stream
                .sorted(Comparator.comparing(ReviewCaseDTO::getSubmitTime).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewSubmitResponse submitReview(ReviewSubmitRequest request) {
        // 获取申请
        String applicationId = request.getApplicationId();
        CreditApplicationAggregate application = pendingReviews.get(applicationId);
        
        if (application == null) {
            throw new IllegalArgumentException("未找到需要审核的申请: " + applicationId);
        }
        
        // 转换决策
        ApprovalDecision decision = ApprovalDecision.fromCode(request.getDecision());
        
        // 执行人工审核
        application.performHumanReview(decision, request.getComment());
        
        // 生成审核ID
        String reviewId = UUID.randomUUID().toString();
        
        // 更新审核状态
        ReviewCaseDTO caseDTO = reviewCases.get(applicationId);
        if (caseDTO != null) {
            // 创建新的DTO，标记为已审核
            reviewCases.put(applicationId, new ReviewCaseDTO(
                    caseDTO.getApplicationId(),
                    caseDTO.getUserId(),
                    caseDTO.getApplicationType(),
                    caseDTO.getRiskLevel(),
                    caseDTO.getCreditScore(),
                    caseDTO.getAiDecision(),
                    caseDTO.getSubmitTime(),
                    true // 标记为已审核
            ));
        }
        
        // 返回响应
        return new ReviewSubmitResponse(
                applicationId,
                reviewId,
                decision.getCode(),
                Instant.now(),
                "completed"
        );
    }
    
    private void initializeMockData() {
        // 这里应该从数据库或其他存储中加载待审核的申请
        // 为了演示，我们创建一些模拟数据
        
        // 模拟案例1 - 高风险
        String appId1 = "APP-" + UUID.randomUUID().toString().substring(0, 8);
        ReviewCaseDTO case1 = new ReviewCaseDTO(
                appId1,
                "user123",
                "personal_loan",
                "HIGH",
                580,
                "reject",
                Instant.now().minusSeconds(3600), // 1小时前
                false
        );
        reviewCases.put(appId1, case1);
        
        // 模拟案例2 - 中等风险
        String appId2 = "APP-" + UUID.randomUUID().toString().substring(0, 8);
        ReviewCaseDTO case2 = new ReviewCaseDTO(
                appId2,
                "user456",
                "business_loan",
                "MEDIUM",
                650,
                "approve",
                Instant.now().minusSeconds(7200), // 2小时前
                false
        );
        reviewCases.put(appId2, case2);
        
        // 模拟案例3 - 已审核
        String appId3 = "APP-" + UUID.randomUUID().toString().substring(0, 8);
        ReviewCaseDTO case3 = new ReviewCaseDTO(
                appId3,
                "user789",
                "personal_loan",
                "MEDIUM",
                630,
                "approve",
                Instant.now().minusSeconds(10800), // 3小时前
                true
        );
        reviewCases.put(appId3, case3);
    }
}