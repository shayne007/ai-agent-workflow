package com.fintech.aiagent.interfaces.rest;

import java.util.List;
import java.util.Collections;

public record UploadResult(
    boolean success,
    String message,
    int filesProcessed,
    String timestamp,
    List<String> missingFields,
    List<String> uploadedFiles
) {
    // Constructor with default values for the new fields
    public UploadResult(boolean success, String message, int filesProcessed, String timestamp) {
        this(success, message, filesProcessed, timestamp, Collections.emptyList(), Collections.emptyList());
    }
    
    // Getter methods for the new fields
    public List<String> missingFields() {
        return missingFields;
    }
    
    public List<String> uploadedFiles() {
        return uploadedFiles;
    }
}