package aiagent.infrastructure.messaging;

import org.springframework.stereotype.Service;

@Service
public class MessageQueueService {
    public void sendMessage(String message) {
        // Simulate message queue operation
        System.out.println("Message sent to queue: " + message);
    }

    public String receiveMessage() {
        // Simulate message queue operation
        return "Simulated received message";
    }
} 