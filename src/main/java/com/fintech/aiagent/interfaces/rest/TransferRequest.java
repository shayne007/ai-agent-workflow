package com.fintech.aiagent.interfaces.rest;

/**
 * Request object for transferring to a human agent.
 */
public record TransferRequest(
    String sessionId,
    String reason
) {}