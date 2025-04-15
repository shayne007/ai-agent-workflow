package com.fintech.aiagent.application.service;

import com.fintech.aiagent.application.dto.ApplicationSubmitRequest;
import com.fintech.aiagent.application.dto.ApplicationSubmitResponse;

public interface ApplicationService {
    ApplicationSubmitResponse submitApplication(ApplicationSubmitRequest request);
}