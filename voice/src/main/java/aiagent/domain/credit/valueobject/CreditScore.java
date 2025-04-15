package aiagent.domain.credit.valueobject;

public record CreditScore(int score) {
    public CreditScore {
        if (score < 0 || score > 900) {
            throw new IllegalArgumentException("信用分数必须在0-900之间");
        }
    }

    public RiskLevel getRiskLevel() {
        if (score >= 700) {
            return RiskLevel.LOW;
        }
        if (score >= 600) {
            return RiskLevel.MEDIUM;
        }
        return RiskLevel.HIGH;
    }
}