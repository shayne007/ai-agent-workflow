package com.fintech.aiagent.domain.customer.service;

import org.springframework.core.io.Resource;

public interface VoiceService {
    Resource synthesize(String text, String voiceType, double speed);
} 