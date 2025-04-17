package aiagent;

import org.springframework.ai.autoconfigure.mcp.client.SseHttpClientTransportAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableAutoConfiguration(exclude = { SseHttpClientTransportAutoConfiguration.class })
public class CustomServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(CustomServiceApp.class, args);
    }
} 