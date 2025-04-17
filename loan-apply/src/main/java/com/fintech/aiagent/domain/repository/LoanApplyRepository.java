package com.fintech.aiagent.domain.repository;

import com.fintech.aiagent.domain.aggregate.LoanApplyAggregate;
import java.util.List;

public interface LoanApplyRepository{
    
    LoanApplyAggregate get(String applicantId);
    List<LoanApplyAggregate> save(LoanApplyAggregate applicantId);
}