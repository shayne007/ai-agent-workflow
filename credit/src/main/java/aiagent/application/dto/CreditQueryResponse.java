package aiagent.application.dto;

import java.util.List;

public class CreditQueryResponse {
    private int creditScore;
    private List<LoanHistoryDTO> loanHistory;
    private boolean fromCache;

    public CreditQueryResponse(int creditScore, List<LoanHistoryDTO> loanHistory, boolean isCache) {
        this.creditScore = creditScore;
        this.loanHistory = loanHistory;
        this.fromCache = isCache;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public List<LoanHistoryDTO> getLoanHistory() {
        return loanHistory;
    }

    public boolean isFromCache() {
        return fromCache;
    }

    public static class LoanHistoryDTO {
        private double amount;
        private String status;

        public LoanHistoryDTO(double amount, String status) {
            this.amount = amount;
            this.status = status;
        }

        public double getAmount() {
            return amount;
        }

        public String getStatus() {
            return status;
        }
    }
}