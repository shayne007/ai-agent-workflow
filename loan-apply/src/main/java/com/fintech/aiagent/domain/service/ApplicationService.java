package com.fintech.aiagent.domain.service;

import com.fintech.aiagent.interfaces.rest.ApplicationStatus;
import com.fintech.aiagent.interfaces.rest.UploadResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ApplicationService {
    ApplicationStatus getApplicationStatus(String applicationId);
    UploadResult uploadMaterials(String applicationId, List<MultipartFile> files, String documentType);
} 