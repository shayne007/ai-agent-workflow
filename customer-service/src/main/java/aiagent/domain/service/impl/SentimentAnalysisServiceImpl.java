package aiagent.domain.service.impl;

import aiagent.domain.service.SentimentAnalysisService;
import aiagent.infrastructure.ai.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SentimentAnalysisServiceImpl implements SentimentAnalysisService {

    private final AIService aiService;

    @Autowired
    public SentimentAnalysisServiceImpl(AIService aiService) {
        this.aiService = aiService;
    }

    @Override
    public double analyzeSentiment(String text, String language) {
        // Language parameter is ignored in this simple implementation
        return aiService.analyzeSentiment(text);
    }
}