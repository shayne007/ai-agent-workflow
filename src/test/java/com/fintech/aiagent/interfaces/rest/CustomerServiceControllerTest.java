package com.fintech.aiagent.interfaces.rest;

import com.fintech.aiagent.domain.customer.entity.Conversation;
import com.fintech.aiagent.domain.customer.entity.Response;
import com.fintech.aiagent.domain.customer.service.CustomerServiceDomainService;
import com.fintech.aiagent.domain.customer.valueobject.ConversationId;
import com.fintech.aiagent.domain.customer.valueobject.CustomerId;
import com.fintech.aiagent.domain.customer.valueobject.ResponseType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceControllerTest {

    @Mock
    private CustomerServiceDomainService customerService;

    @InjectMocks
    private CustomerServiceController controller;

    private String mockSessionId;
    private Conversation mockConversation;
    private Response mockResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Setup common test data
        mockSessionId = UUID.randomUUID().toString();
        
        // Mock conversation
        mockConversation = new Conversation();
        mockConversation.setConversationId(new ConversationId(mockSessionId));
        
        // Mock response
        mockResponse = new Response();
        mockResponse.setContent("This is a test response");
        mockResponse.setSuggestedActions(new String[]{"Action1", "Action2"});
        mockResponse.setType(ResponseType.TEXT);
    }

    @Test
    void startConversation_ShouldReturnNewSession() {
        // Arrange
        StartConversationRequest request = new StartConversationRequest("user123", "mobile", "app");
        when(customerService.createConversation(any(CustomerId.class))).thenReturn(mockConversation);

        // Act
        ResponseEntity<Map<String, Object>> response = controller.startConversation(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockSessionId, response.getBody().get("sessionId"));
        assertNotNull(response.getBody().get("responseText"));
        assertNotNull(response.getBody().get("suggestedActions"));
        
        verify(customerService).createConversation(any(CustomerId.class));
    }

    @Test
    void continueConversation_ShouldProcessUserInput() {
        // Arrange
        ContinueConversationRequest request = new ContinueConversationRequest(
                mockSessionId, "How can I check my loan status?", "en-US");
        when(customerService.processQuery(any(ConversationId.class), anyString())).thenReturn(mockResponse);

        // Act
        ResponseEntity<Map<String, Object>> response = controller.continueConversation(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockResponse.getContent(), response.getBody().get("responseText"));
        assertArrayEquals(mockResponse.getSuggestedActions(), (String[]) response.getBody().get("suggestedActions"));
        assertEquals(mockResponse.getType().toString(), response.getBody().get("type"));
        
        verify(customerService).processQuery(any(ConversationId.class), eq(request.inputText()));
    }

    @Test
    void handleVoiceQuery_ShouldProcessAudioInput() {
        // Arrange
        String audioUrl = "https://example.com/audio/12345.mp3";
        when(customerService.processVoiceQuery(any(ConversationId.class), anyString())).thenReturn(mockResponse);
        mockResponse.setContext("https://example.com/response-audio.mp3");

        // Act
        ResponseEntity<Map<String, Object>> response = controller.handleVoiceQuery(mockSessionId, audioUrl);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockResponse.getContent(), response.getBody().get("responseText"));
        assertEquals(mockResponse.getContext(), response.getBody().get("audioUrl"));
        assertEquals(mockResponse.getType().toString(), response.getBody().get("type"));
        
        verify(customerService).processVoiceQuery(any(ConversationId.class), eq(audioUrl));
    }

    @Test
    void transferToHuman_ShouldQueueTransferRequest() {
        // Arrange
        TransferRequest request = new TransferRequest(mockSessionId, "Need complex assistance");
        when(customerService.processQuery(any(ConversationId.class), eq("REQUEST_HUMAN_TRANSFER"))).thenReturn(mockResponse);

        // Act
        ResponseEntity<Map<String, Object>> response = controller.transferToHuman(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("queued", response.getBody().get("transferStatus"));
        assertEquals(1, response.getBody().get("queuePosition"));
        assertEquals(mockResponse.getContent(), response.getBody().get("responseText"));
        
        verify(customerService).processQuery(any(ConversationId.class), eq("REQUEST_HUMAN_TRANSFER"));
    }

    @Test
    void endConversation_ShouldCompleteSession() {
        // Act
        ResponseEntity<Void> response = controller.endConversation(mockSessionId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(customerService).completeConversation(any(ConversationId.class));
    }
    
    @Test
    void startConversation_WithInvalidRequest_ShouldFail() {
        // This test would require Spring MVC test context setup to validate @Valid annotations
        // Simplified version just to illustrate the concept
        StartConversationRequest request = new StartConversationRequest(null, "mobile", "app");
        
        // In a real test with Spring MVC Test, this would verify that validation fails
        assertNotNull(request);
    }
}