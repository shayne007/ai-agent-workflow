package com.fintech.aiagent.domain.customer.service;

import com.fintech.aiagent.domain.customer.entity.Conversation;
import com.fintech.aiagent.domain.customer.entity.Response;
import com.fintech.aiagent.domain.customer.valueobject.ConversationId;
import com.fintech.aiagent.domain.customer.valueobject.CustomerId;

/**
 * Domain service for customer service operations.
 */
public interface CustomerServiceDomainService {
    
    /**
     * Creates a new conversation for a customer.
     *
     * @param customerId The customer ID
     * @return The created conversation
     */
    Conversation createConversation(CustomerId customerId);
    
    /**
     * Processes a text query from the customer.
     *
     * @param conversationId The conversation ID
     * @param query The customer's query
     * @return The response to the query
     */
    Response processQuery(ConversationId conversationId, String query);
    
    /**
     * Processes a voice query from the customer.
     *
     * @param conversationId The conversation ID
     * @param audioUrl URL to the audio file
     * @return The response to the voice query
     */
    Response processVoiceQuery(ConversationId conversationId, String audioUrl);
    
    /**
     * Completes a conversation.
     *
     * @param conversationId The conversation ID
     */
    void completeConversation(ConversationId conversationId);
}