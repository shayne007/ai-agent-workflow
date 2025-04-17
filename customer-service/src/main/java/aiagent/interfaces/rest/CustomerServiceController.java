package aiagent.interfaces.rest;

import aiagent.domain.entity.Conversation;
import aiagent.domain.entity.Response;
import aiagent.domain.service.CustomerServiceDomainService;
import aiagent.domain.valueobject.ConversationId;
import aiagent.domain.valueobject.CustomerId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dialog")
@RequiredArgsConstructor
public class CustomerServiceController {
    private final CustomerServiceDomainService customerService;

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startConversation(@Valid @RequestBody StartConversationRequest request) {
        Conversation conversation = customerService.createConversation(new CustomerId(request.userId()));

        return ResponseEntity.ok(Map.of("sessionId", conversation.getConversationId().getId(), "responseText",
            "您好！我是AI智能助手，请问有什么可以帮您？", "suggestedActions",
            new String[] {"查询进度", "申请贷款", "咨询产品"}));
    }

    @PostMapping("/continue")
    public ResponseEntity<Map<String, Object>> continueConversation(
        @Valid @RequestBody ContinueConversationRequest request) {
        Response response = customerService.processQuery(new ConversationId(request.sessionId()), request.inputText());

        return ResponseEntity.ok(
            Map.of("responseText", response.getContent(), "suggestedActions", response.getSuggestedActions(), "type",
                response.getType().toString()));
    }

    @PostMapping("/voice/recognize")
    public ResponseEntity<Map<String, Object>> handleVoiceQuery(@RequestParam("sessionId") String sessionId,
        @RequestParam("audioUrl") String audioUrl) {
        Response response = customerService.processVoiceQuery(new ConversationId(sessionId), audioUrl);

        return ResponseEntity.ok(
            Map.of("responseText", response.getContent(), "audioUrl", response.getContext(), "type",
                response.getType().toString()));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Map<String, Object>> transferToHuman(@Valid @RequestBody TransferRequest request) {
        Response response =
            customerService.processQuery(new ConversationId(request.sessionId()), "REQUEST_HUMAN_TRANSFER");

        return ResponseEntity.ok(
            Map.of("transferStatus", "queued", "queuePosition", 1, "responseText", response.getContent()));
    }

    @PostMapping("/end")
    public ResponseEntity<Void> endConversation(@RequestParam("sessionId") String sessionId) {
        customerService.completeConversation(new ConversationId(sessionId));
        return ResponseEntity.ok().build();
    }
}

// Make sure the StartConversationRequest is defined here as an inner class or record
record StartConversationRequest(String userId, String deviceType, String channel) {
}
record ContinueConversationRequest(
    String sessionId,
    String inputText,
    String language
) {}
/**
 * Request object for transferring to a human agent.
 */
 record TransferRequest(
    String sessionId,
    String reason
) {}