package com.fintech.aiagent.domain.service.impl;

import com.fintech.aiagent.domain.service.ApplicationService;
import com.fintech.aiagent.interfaces.rest.ApplicationStatus;
import com.fintech.aiagent.interfaces.rest.UploadResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Service("customerApplicationServiceImpl")  // 修改Bean名称
public class ApplicationServiceImpl implements ApplicationService {

    @Override
    public ApplicationStatus getApplicationStatus(String applicationId) {
        // This is a simple implementation for development/testing
        return new ApplicationStatus(
            "IN_PROGRESS",
            Instant.now(),
            75,
            "2025-04-16T12:00:00Z"
        );
    }

    @Override
    public UploadResult uploadMaterials(String applicationId, List<MultipartFile> files, String documentType) {
        // Simple implementation for development/testing
        return new UploadResult(
            true,
            "Files uploaded successfully",
            files.size(),
            Instant.now().toString()
        );
    }
}