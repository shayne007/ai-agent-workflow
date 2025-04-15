package com.fintech.aiagent.domain.customer.unit;

import com.fintech.aiagent.domain.customer.entity.Conversation;
import com.fintech.aiagent.domain.customer.entity.Response;
import com.fintech.aiagent.domain.customer.service.CustomerServiceDomainService;
import com.fintech.aiagent.domain.customer.service.impl.CustomerServiceDomainServiceImpl;
import com.fintech.aiagent.domain.customer.valueobject.CustomerId;
import com.fintech.aiagent.domain.customer.valueobject.ResponseType;
import com.fintech.aiagent.infrastructure.ai.AIService;
import com.fintech.aiagent.infrastructure.ai.AgenticAIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class CustomerServiceTest {

    // Use the implementation class instead of the interface
    private CustomerServiceDomainService customerService;
    @MockBean
    private AIService aiService;
    @BeforeEach
    void setUp() {
        // Initialize with the implementation class
        customerService = new CustomerServiceDomainServiceImpl((AgenticAIService)aiService);
    }

    @Test
    void createConversation_ShouldReturnNewConversation() {
        // Arrange
        CustomerId customerId = new CustomerId("user123");

        // Act
        Conversation conversation = customerService.createConversation(customerId);

        // Assert
        assertNotNull(conversation);
        assertNotNull(conversation.getConversationId());
        assertEquals(customerId, conversation.getCustomerId());
        assertTrue(conversation.isActive());
        assertNotNull(conversation.getStartTime());
    }

    @Test
    void processQuery_ShouldReturnResponse() {
        // Arrange
        CustomerId customerId = new CustomerId("user123");
        Conversation conversation = customerService.createConversation(customerId);
        String query = "How can I check my loan status?";

        // Act
        Response response = customerService.processQuery(conversation.getConversationId(), query);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getContent());
        assertEquals(ResponseType.TEXT, response.getType());
        assertNotNull(response.getSuggestedActions());
    }

    // Add more tests as needed
}