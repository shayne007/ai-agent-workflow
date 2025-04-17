package aiagent.interfaces.rest;

import aiagent.domain.service.SentimentAnalysisService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sentiment")
@RequiredArgsConstructor
public class SentimentController {
    
    private final SentimentAnalysisService sentimentAnalysisService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> analyzeSentiment(@Valid @RequestBody SentimentAnalysisRequest request) {
        double score = sentimentAnalysisService.analyzeSentiment(request.text(), request.language());
        SentimentResult result = SentimentResult.fromScore(score, request.text(), request.language());
        
        return ResponseEntity.ok(Map.of(
            "sentiment", result.sentiment(),
            "score", result.score(),
            "recommendedResponse", result.recommendedResponse(),
            "emotions", result.emotions()
        ));
    }
    
    // Removing the second endpoint that uses the missing SentimentRequest class
}

record SentimentAnalysisRequest(
    String text,
    String language,
    String sessionId
) {}