package com.fintech.aiagent.domain.customer.service;

public interface SentimentAnalysisService {
    // Updated to match the controller's usage with two parameters
    double analyzeSentiment(String text, String language);
}