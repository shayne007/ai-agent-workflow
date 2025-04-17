package com.fintech.aiagent.domain.repository;

import com.fintech.aiagent.domain.entity.CreditReport;

/**
 * TODO
 *
 * @since 2025/4/17
 */
public interface CreditReportRepository {
    CreditReport get(String userId);
}
