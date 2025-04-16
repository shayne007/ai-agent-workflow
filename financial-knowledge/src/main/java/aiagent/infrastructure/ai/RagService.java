package aiagent.infrastructure.ai;

import org.springframework.ai.chat.model.ChatResponse;
import reactor.core.publisher.Flux;

/**
 * TODO
 *
 * @since 2025/4/16
 */
public interface RagService {
    void importDocuments();

    Flux<ChatResponse> retrieve(String message);
}
