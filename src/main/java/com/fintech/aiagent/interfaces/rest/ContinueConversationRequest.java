package com.fintech.aiagent.interfaces.rest;

/**
 * Request object for continuing an existing conversation.
 */
public record ContinueConversationRequest(
    String sessionId,
    String inputText,
    String language
) {}