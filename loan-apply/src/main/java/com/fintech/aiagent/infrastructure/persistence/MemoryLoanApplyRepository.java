package com.fintech.aiagent.infrastructure.persistence;

import com.fintech.aiagent.domain.aggregate.LoanApplyAggregate;
import com.fintech.aiagent.domain.repository.LoanApplyRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * TODO
 *
 * @since 2025/4/17
 */
@Repository
public class MemoryLoanApplyRepository implements LoanApplyRepository {
    private final Map<String, LoanApplyAggregate> pendingReviews = new HashMap<>();
    @Override
    public LoanApplyAggregate get(String applicationId) {
        return pendingReviews.get(applicationId);
    }

    @Override
    public List<LoanApplyAggregate> save(LoanApplyAggregate applicantId) {
        pendingReviews.put(applicantId.getApplicationId().getId(), applicantId);
        return null;
    }
}
