package aiagent.application.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * TODO
 *
 * @since 2025/4/17
 */
@Service
public class ThirdPartyCreditService {
    private final WebClient webClient;

    public ThirdPartyCreditService(WebClient.Builder builder) {
        // 调用三方系统接口，获取信用评分
        this.webClient = builder.baseUrl("http://localhost:8080/credit").build();
    }

    @Tool(description = "根据用户ID获取征信报告")
    public String getCreditByUserId(@ToolParam(description = "User ID") String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        // query credit report from third-party system
        // or from cache
        if (userId.equalsIgnoreCase("123456")) {
            webClient.get().uri("/{userId}", userId).retrieve().bodyToMono(String.class).block();
        }
        // TODO: 从缓存中获取信用报告
        return "{\"creditScore\":720,\"loanHistory\":[{\"amount\":50000.0,\"status\":\"repaid\"},{\"amount\":10000.0,\"status\":\"active\"}],\"fromCache\":true}";
    }
}
