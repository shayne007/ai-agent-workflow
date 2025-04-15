package aiagent.domain.voice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerQuery {
    private String id;
    private String conversationId;
    private String text;
    private String intent;
    private Instant timestamp;
    private String language;
    private String audioUrl;
    private boolean isVoiceQuery;
}