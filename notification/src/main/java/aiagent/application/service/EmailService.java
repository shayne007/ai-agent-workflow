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
public class EmailService {
    private final WebClient webClient;

    public EmailService(WebClient.Builder builder) {
        // 调用三方系统接口，获取信用评分
        this.webClient = builder.baseUrl("http://localhost:8080/email").build();
    }

    @Tool(description = "给指定用户发送邮件")
    public String sendEmail(@ToolParam(description = "User ID") String userId,
        @ToolParam(description = "Email Content") String content) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        System.out.println("Sending email to user " + userId + " with content " + content);
        return "SUCCESS";
    }
}
