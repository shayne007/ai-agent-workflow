package aiagent.interfaces.rest;

import aiagent.infrastructure.ai.RagService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
        defaultValue = "hello,what is the knowledge base documents content about?") String message,
        HttpServletResponse response) {
        // 设置响应编码，方式 stream 响应乱码。
        response.setCharacterEncoding("UTF-8");

        if (!StringUtils.hasText(message)) {
            return Flux.just("prompt is null.");
        }
        return cloudRagService.retrieve(message).map(x -> x.getResult().getOutput().getText());
    }
}