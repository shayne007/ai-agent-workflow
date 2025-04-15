package aiagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class CreditApp {

    public static void main(String[] args) {
        SpringApplication.run(CreditApp.class, args);
    }
} 