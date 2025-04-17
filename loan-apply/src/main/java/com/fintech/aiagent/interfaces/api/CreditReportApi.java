package com.fintech.aiagent.interfaces.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.aiagent.domain.entity.CreditReport;
import com.fintech.aiagent.domain.repository.CreditReportRepository;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import org.springframework.stereotype.Repository;

/**
 * TODO
 *
 * @since 2025/4/17
 */
@Repository
public class CreditReportApi implements CreditReportRepository {
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public CreditReport get(String userId) {
        try {
            String body = HttpClient.newHttpClient()
                .send(HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/api/v1/credit/query/"+ userId)).build(),
                    BodyHandlers.ofString()).body();
            
            // Use Jackson's ObjectMapper to convert JSON string to CreditReport object
            CreditReport report = objectMapper.readValue(body, CreditReport.class);
            return report;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
