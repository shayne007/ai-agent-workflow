package com.fintech.aiagent.interfaces.rest;

import java.time.Instant;

public record ApplicationStatus(
    String status,
    Instant updateTime,
    int progress,
    String estimatedCompletionTime
) {} 