package com.fintech.aiagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class AiAgentWorkflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiAgentWorkflowApplication.class, args);
    }
} 