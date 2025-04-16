package com.fintech.aiagent.interfaces.rest;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Collections;

public record SentimentResult(
    double score,
    String sentiment,
    String text,
    String language,
    Instant timestamp,
    String recommendedResponse,
    List<String> emotions
) {
    public static SentimentResult fromScore(double score, String text, String language) {
        String sentiment = "neutral";
        String recommendedResponse = "How can I assist you further?";
        List<String> emotions = Collections.emptyList();
        
        if (score > 0.3) {
            sentiment = "positive";
            recommendedResponse = "I'm glad to hear that! How else can I help you?";
            emotions = List.of("happy", "satisfied");
        } else if (score < -0.3) {
            sentiment = "negative";
            recommendedResponse = "I'm sorry to hear that. Let me try to help resolve your issue.";
            emotions = List.of("frustrated", "disappointed");
        }
        
        return new SentimentResult(
            score,
            sentiment,
            text,
            language,
            Instant.now(),
            recommendedResponse,
            emotions
        );
    }
}