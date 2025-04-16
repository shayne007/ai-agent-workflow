package com.fintech.aiagent.interfaces.rest;

import com.fintech.aiagent.application.dto.ApplicationSubmitRequest;
import com.fintech.aiagent.application.dto.ApplicationSubmitResponse;
import com.fintech.aiagent.application.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/application")
public class ApplicationController {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApplicationSubmitResponse> submitApplication(
            @RequestParam("userId") String userId,
            @RequestParam("applicationType") String applicationType,
            @RequestParam("files") List<MultipartFile> files) {
        
        ApplicationSubmitRequest request = new ApplicationSubmitRequest();
        request.setUserId(userId);
        request.setApplicationType(applicationType);
        request.setFiles(files);
        
        ApplicationSubmitResponse response = applicationService.submitApplication(request);
        return ResponseEntity.ok(response);
    }
}