package com.fintech.aiagent.infrastructure.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

@Service
@Primary
public class AgenticAIService implements AIService {
    private final ChatClient chatClient;
    
    @Autowired
    public AgenticAIService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }
    
    @Override
    public double analyzeSentiment(String text) {
        return 0;
    }
    
    @Override
    public String detectIntent(String text) {
        return "general_inquiry";
    }
    
    @Override
    public String generateResponse(String query, String currentIntent) {
        try {
            return chatClient.prompt()
                .user(query)
                .call()
                .content();
        } catch (Exception e) {
            // Fallback for testing
            return "I'm sorry, I couldn't process that request. How else can I help you?";
        }
    }
    
    @Override
    public String performASR(String audioUrl) {
        return "Simulated speech recognition result";
    }
    
    @Override
    public String performTTS(String text) {
        return "simulated_audio_url.mp3";
    }
}
