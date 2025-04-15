package com.fintech.aiagent.domain.credit.entity;

import com.fintech.aiagent.domain.credit.valueobject.CreditScore;
import com.fintech.aiagent.domain.customer.valueobject.CustomerId;

import java.time.Instant;
import java.util.List;

public class CreditReport {
    private String reportId;
    private CustomerId customerId;
    private CreditScore creditScore;
    private List<LoanHistory> loanHistory;
    private Instant queryTime;
    private boolean fromCache;

    public CreditReport(String reportId, CustomerId customerId, CreditScore creditScore, 
                       List<LoanHistory> loanHistory, boolean fromCache) {
        this.reportId = reportId;
        this.customerId = customerId;
        this.creditScore = creditScore;
        this.loanHistory = loanHistory;
        this.queryTime = Instant.now();
        this.fromCache = fromCache;
    }

    // 内部类：贷款历史
    public static class LoanHistory {
        private double amount;
        private String status; // repaid, overdue, active
        private int overdueCount;

        public LoanHistory(double amount, String status, int overdueCount) {
            this.amount = amount;
            this.status = status;
            this.overdueCount = overdueCount;
        }

        public double getAmount() {
            return amount;
        }

        public String getStatus() {
            return status;
        }

        public int getOverdueCount() {
            return overdueCount;
        }
    }

    // Getters
    public String getReportId() {
        return reportId;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public CreditScore getCreditScore() {
        return creditScore;
    }

    public List<LoanHistory> getLoanHistory() {
        return loanHistory;
    }

    public Instant getQueryTime() {
        return queryTime;
    }

    public boolean isFromCache() {
        return fromCache;
    }
}