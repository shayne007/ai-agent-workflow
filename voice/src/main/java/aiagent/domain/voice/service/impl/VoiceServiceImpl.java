package aiagent.domain.voice.service.impl;

import aiagent.domain.voice.service.VoiceService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class VoiceServiceImpl implements VoiceService {

    @Override
    public Resource synthesize(String text, String voiceType, double speed) {
        // This is a simple mock implementation for development/testing
        // In a real implementation, this would call a TTS service
        
        // For now, just return a mock resource with the text encoded as bytes
        String mockAudioData = "Mock audio data for: " + text + 
                               " (Voice: " + voiceType + 
                               ", Speed: " + speed + ")";
        
        return new ByteArrayResource(mockAudioData.getBytes(StandardCharsets.UTF_8)) {
            @Override
            public String getFilename() {
                return "synthesized_speech.mp3";
            }
        };
    }
}