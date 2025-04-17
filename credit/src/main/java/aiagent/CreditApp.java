package aiagent;

import aiagent.application.service.ThirdPartyCreditService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class CreditApp {

    public static void main(String[] args) {
        SpringApplication.run(CreditApp.class, args);
    }

    @Bean
    public ToolCallbackProvider creditServiceTools(ThirdPartyCreditService creditService) {
        return MethodToolCallbackProvider.builder()
            .toolObjects(creditService)
            .build();
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
} 