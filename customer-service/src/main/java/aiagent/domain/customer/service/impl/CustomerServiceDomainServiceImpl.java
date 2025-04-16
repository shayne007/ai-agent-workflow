package aiagent.domain.customer.service.impl;


import aiagent.domain.customer.entity.Conversation;
import aiagent.domain.customer.entity.Response;
import aiagent.domain.customer.service.CustomerServiceDomainService;
import aiagent.domain.customer.valueobject.ConversationId;
import aiagent.domain.customer.valueobject.CustomerId;
import aiagent.domain.customer.valueobject.ResponseType;
import aiagent.infrastructure.ai.AgenticAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class CustomerServiceDomainServiceImpl implements CustomerServiceDomainService {
    
    // Simple in-memory storage for conversations
    private final Map<String, Conversation> conversations = new HashMap<>();
    @Autowired
    private final AgenticAIService aiService;

    public CustomerServiceDomainServiceImpl(AgenticAIService aiService) {
        this.aiService = aiService;
    }

    @Override
    public Conversation createConversation(CustomerId customerId) {
        String conversationId = UUID.randomUUID().toString();
        Conversation conversation = new Conversation();
        conversation.setConversationId(new ConversationId(conversationId));
        conversation.setCustomerId(customerId);
        conversation.setStartTime(Instant.now());
        conversation.setActive(true);
        
        conversations.put(conversationId, conversation);
        return conversation;
    }
    
    @Override
    public Response processQuery(ConversationId conversationId, String query) {
        Conversation conversation = conversations.get(conversationId.getId());
        if (conversation == null) {
            throw new IllegalArgumentException("Conversation not found: " + conversationId);
        }
        
        conversation.addMessage(query);
        conversation.incrementTurnCount();
        String generateResponse = aiService.generateResponse(query, "");

        // Simple mock response
        Response response = new Response();
        String content = "This is a response to: " + query + generateResponse;
        response.setContent(content);
        response.setSuggestedActions(new String[]{"Option 1", "Option 2"});
        response.setType(ResponseType.TEXT);
        
        return response;
    }
    
    @Override
    public Response processVoiceQuery(ConversationId conversationId, String audioUrl) {
        Conversation conversation = conversations.get(conversationId.getId());
        if (conversation == null) {
            throw new IllegalArgumentException("Conversation not found: " + conversationId);
        }
        
        conversation.addMessage("Voice query: " + audioUrl);
        conversation.incrementTurnCount();
        
        // Simple mock response
        Response response = new Response();
        response.setContent("I've processed your voice query");
        response.setSuggestedActions(new String[]{"Option 1", "Option 2"});
        response.setType(ResponseType.AUDIO);
        response.setContext("https://example.com/response-audio.mp3");
        
        return response;
    }
    
    @Override
    public void completeConversation(ConversationId conversationId) {
        Conversation conversation = conversations.get(conversationId.getId());
        if (conversation == null) {
            throw new IllegalArgumentException("Conversation not found: " + conversationId);
        }
        
        conversation.setActive(false);
        conversation.setEndTime(Instant.now());
    }
}