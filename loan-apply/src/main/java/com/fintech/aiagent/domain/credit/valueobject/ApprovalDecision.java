package com.fintech.aiagent.domain.credit.valueobject;

public enum ApprovalDecision {
    APPROVED("approve"),
    REJECTED("reject"),
    PENDING_REVIEW("review");

    private final String code;

    ApprovalDecision(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ApprovalDecision fromCode(String code) {
        for (ApprovalDecision decision : values()) {
            if (decision.code.equals(code)) {
                return decision;
            }
        }
        throw new IllegalArgumentException("未知的决策代码: " + code);
    }
}