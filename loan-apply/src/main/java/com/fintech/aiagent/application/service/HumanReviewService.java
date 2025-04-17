package com.fintech.aiagent.application.service;

import com.fintech.aiagent.application.dto.ReviewCaseDTO;
import com.fintech.aiagent.application.dto.ReviewSubmitRequest;
import com.fintech.aiagent.application.dto.ReviewSubmitResponse;

import java.util.List;

public interface HumanReviewService {
    List<ReviewCaseDTO> getReviewCases(String riskLevel, int limit);
    ReviewSubmitResponse submitReview(ReviewSubmitRequest request);
}