package com.fintech.aiagent.interfaces.rest;

import com.fintech.aiagent.domain.customer.service.VoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/voice")
@RequiredArgsConstructor
public class VoiceController {
    
    private final VoiceService voiceService;

    @PostMapping("/synthesize")
    public ResponseEntity<Resource> synthesizeVoice(@Valid @RequestBody VoiceSynthesisRequest request) {
        Resource audioResource = voiceService.synthesize(
            request.text(),
            request.voiceType(),
            request.speed()
        );

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("audio/mpeg"))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"speech.mp3\"")
            .body(audioResource);
    }
}

record VoiceSynthesisRequest(
    String text,
    String voiceType,
    double speed
) {} 