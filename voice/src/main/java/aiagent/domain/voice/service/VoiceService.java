package aiagent.domain.voice.service;

import org.springframework.core.io.Resource;

public interface VoiceService {
    Resource synthesize(String text, String voiceType, double speed);
} 