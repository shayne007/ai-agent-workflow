package com.fintech.aiagent.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.aiagent.domain.customer.entity.Conversation;
import com.fintech.aiagent.domain.customer.entity.Response;
import com.fintech.aiagent.domain.customer.service.CustomerServiceDomainService;
import com.fintech.aiagent.domain.customer.valueobject.ConversationId;
import com.fintech.aiagent.domain.customer.valueobject.CustomerId;
import com.fintech.aiagent.domain.customer.valueobject.ResponseType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerServiceController.class)
class CustomerServiceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerServiceDomainService customerService;

    private String mockSessionId;
    private Conversation mockConversation;
    private Response mockResponse;

    @BeforeEach
    void setUp() {
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
    void startConversation_ShouldReturnNewSession() throws Exception {
        // Arrange
        StartConversationRequest request = new StartConversationRequest("user123", "mobile", "app");
        when(customerService.createConversation(any(CustomerId.class))).thenReturn(mockConversation);

        // Act & Assert
        mockMvc.perform(post("/api/v1/dialog/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(mockSessionId))
                .andExpect(jsonPath("$.responseText").exists())
                .andExpect(jsonPath("$.suggestedActions").isArray());
    }

    @Test
    void continueConversation_ShouldProcessUserInput() throws Exception {
        // Arrange
        ContinueConversationRequest request = new ContinueConversationRequest(
                mockSessionId, "How can I check my loan status?", "en-US");
        when(customerService.processQuery(any(ConversationId.class), any(String.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/dialog/continue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseText").value(mockResponse.getContent()))
                .andExpect(jsonPath("$.suggestedActions").isArray())
                .andExpect(jsonPath("$.type").value(mockResponse.getType().toString()));
    }

    @Test
    void handleVoiceQuery_ShouldProcessAudioInput() throws Exception {
        // Arrange
        String audioUrl = "https://example.com/audio/12345.mp3";
        mockResponse.setContext("https://example.com/response-audio.mp3");
        when(customerService.processVoiceQuery(any(ConversationId.class), any(String.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/dialog/voice/recognize")
                .param("sessionId", mockSessionId)
                .param("audioUrl", audioUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseText").value(mockResponse.getContent()))
                .andExpect(jsonPath("$.audioUrl").value(mockResponse.getContext()))
                .andExpect(jsonPath("$.type").value(mockResponse.getType().toString()));
    }
}