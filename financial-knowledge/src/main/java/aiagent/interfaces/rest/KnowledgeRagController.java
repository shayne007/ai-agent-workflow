package aiagent.interfaces.rest;

import aiagent.infrastructure.ai.RagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/knowledge")
public class KnowledgeRagController {

    private final RagService cloudRagService;

    @Autowired
    public KnowledgeRagController(RagService cloudRagService) {
        this.cloudRagService = cloudRagService;
    }

    @GetMapping("/init")
    public void initInternal() {
        cloudRagService.importDocuments();
    }

    @PostMapping("/import")
    public void importExternal() {
        cloudRagService.importDocuments();
    }

    @GetMapping("/chat")
    public Flux<String> generate(@RequestParam(value = "message",
        defaultValue = "hello,what is the knowledge base documents content about?") String message) {
        return cloudRagService.retrieve(message).map(x -> x.getResult().getOutput().getText());
    }
}