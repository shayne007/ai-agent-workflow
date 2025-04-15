package com.fintech.aiagent.domain.customer.integration;

import com.fintech.aiagent.domain.customer.entity.Conversation;
import com.fintech.aiagent.domain.customer.entity.Response;
import com.fintech.aiagent.domain.customer.repository.ConversationRepository;
import com.fintech.aiagent.domain.customer.service.CustomerServiceDomainService;
import com.fintech.aiagent.domain.customer.valueobject.ConversationId;
import com.fintech.aiagent.domain.customer.valueobject.CustomerId;
import com.fintech.aiagent.infrastructure.messaging.MessageQueueService;
import com.fintech.aiagent.infrastructure.ai.AIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Customer Service Integration Tests")
class CustomerServiceIntegrationTest {

    @Autowired
    private CustomerServiceDomainService customerService;

    @Autowired
    private ConversationRepository conversationRepository;

    @MockBean
    private MessageQueueService messageQueueService;

    @MockBean
    private AIService aiService;

    private CustomerId customerId;
    private ConversationId conversationId;

    @BeforeEach
    void setUp() {
        customerId = new CustomerId("customer123");
        conversationId = new ConversationId("conv123");
        conversationRepository.deleteAll();

        // Setup common mock behaviors
        when(aiService.analyzeSentiment(any())).thenReturn(0.0);
        when(aiService.detectIntent(any())).thenReturn("GENERAL_QUERY");
        when(aiService.generateResponse(any(), any())).thenReturn("Mock response");
        when(aiService.performASR(any())).thenReturn("Mock ASR result");
        when(aiService.performTTS(any())).thenReturn("mock-audio-url");
    }

    @Test
    @DisplayName("Should handle complete conversation flow")
    void shouldHandleCompleteConversationFlow() {
        // Given
        Conversation conversation = customerService.createConversation(customerId);
        assertNotNull(conversation);

        // When
        Response response1 = customerService.processQuery(conversation.getConversationId(), "Hello");
        Response response2 = customerService.processQuery(conversation.getConversationId(), "How are you?");

        // Then
        assertNotNull(response1);
        assertNotNull(response2);
        Conversation updatedConversation = conversationRepository.findById(conversation.getConversationId().getId())
                .orElseThrow(() -> new IllegalStateException("Conversation not found"));
        assertEquals(2, updatedConversation.getTurnCount());
        verify(messageQueueService, times(2)).sendMessage(any());
    }

    @Test
    @DisplayName("Should maintain conversation context")
    void shouldMaintainConversationContext() {
        // Given
        Conversation conversation = customerService.createConversation(customerId);

        // When
        customerService.processQuery(conversation.getConversationId(), "What's my account balance?");
        Response response = customerService.processQuery(conversation.getConversationId(), "And my recent transactions?");

        // Then
        assertNotNull(response);
        assertTrue(response.getContent().contains("transactions"));
        Conversation updatedConversation = conversationRepository.findById(conversation.getConversationId().getId())
                .orElseThrow(() -> new IllegalStateException("Conversation not found"));
        assertEquals(2, updatedConversation.getTurnCount());
    }

    @Test
    @DisplayName("Should handle concurrent conversations")
    void shouldHandleConcurrentConversations() {
        // Given
        CustomerId customerId2 = new CustomerId("customer456");
        Conversation conversation1 = customerService.createConversation(customerId);
        Conversation conversation2 = customerService.createConversation(customerId2);

        // When
        Response response1 = customerService.processQuery(conversation1.getConversationId(), "Hello from conversation 1");
        Response response2 = customerService.processQuery(conversation2.getConversationId(), "Hello from conversation 2");

        // Then
        assertNotNull(response1);
        assertNotNull(response2);
        assertNotEquals(response1.getContent(), response2.getContent());
    }

    @Test
    @DisplayName("Should integrate with message queue for async processing")
    void shouldIntegrateWithMessageQueue() {
        // Given
        Conversation conversation = customerService.createConversation(customerId);
        Response response = customerService.processQuery(conversation.getConversationId(), "Process this asynchronously");

        // Then
        assertNotNull(response);
        verify(messageQueueService, times(1)).sendMessage(any());
    }
} 