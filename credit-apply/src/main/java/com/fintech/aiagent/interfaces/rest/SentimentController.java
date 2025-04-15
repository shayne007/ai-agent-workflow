package com.fintech.aiagent.interfaces.rest;

import com.fintech.aiagent.domain.customer.service.SentimentAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

import com.fintech.aiagent.interfaces.rest.SentimentResult;

@RestController
@RequestMapping("/api/v1/sentiment")
@RequiredArgsConstructor
public class SentimentController {
    
    private final SentimentAnalysisService sentimentAnalysisService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> analyzeSentiment(@Valid @RequestBody SentimentAnalysisRequest request) {
        double score = sentimentAnalysisService.analyzeSentiment(request.text(), request.language());
        SentimentResult result = SentimentResult.fromScore(score, request.text(), request.language());
        
        return ResponseEntity.ok(Map.of(
            "sentiment", result.sentiment(),
            "score", result.score(),
            "recommendedResponse", result.recommendedResponse(),
            "emotions", result.emotions()
        ));
    }
    
    // Removing the second endpoint that uses the missing SentimentRequest class
}

record SentimentAnalysisRequest(
    String text,
    String language,
    String sessionId
) {}