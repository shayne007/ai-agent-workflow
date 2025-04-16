package com.fintech.aiagent.application.dto;

import java.util.List;
import java.util.Map;

public class ApplicationSubmitResponse {
    private String applicationId;
    private List<String> missingFields;
    private Map<String, Object> ocrResults;

    public ApplicationSubmitResponse(String applicationId, List<String> missingFields, Map<String, Object> ocrResults) {
        this.applicationId = applicationId;
        this.missingFields = missingFields;
        this.ocrResults = ocrResults;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public List<String> getMissingFields() {
        return missingFields;
    }

    public Map<String, Object> getOcrResults() {
        return ocrResults;
    }
}