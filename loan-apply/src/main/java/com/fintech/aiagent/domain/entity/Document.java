package com.fintech.aiagent.domain.entity;

import java.time.Instant;

public class Document {
    private String documentId;
    private String documentType; // id_card, income_proof, bank_statement
    private String filePath;
    private String fileName;
    private String mimeType;
    private long fileSize;
    private Instant uploadTime;
    private boolean verified;

    public Document(String documentId, String documentType, String filePath, 
                   String fileName, String mimeType, long fileSize) {
        this.documentId = documentId;
        this.documentType = documentType;
        this.filePath = filePath;
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
        this.uploadTime = Instant.now();
        this.verified = false;
    }

    public void markAsVerified() {
        this.verified = true;
    }

    // Getters
    public String getDocumentId() {
        return documentId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public Instant getUploadTime() {
        return uploadTime;
    }

    public boolean isVerified() {
        return verified;
    }
}