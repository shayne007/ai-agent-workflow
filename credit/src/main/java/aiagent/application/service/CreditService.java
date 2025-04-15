package aiagent.application.service;

import aiagent.application.dto.CreditQueryResponse;

public interface CreditService {
    CreditQueryResponse queryCreditData(String userId);
}