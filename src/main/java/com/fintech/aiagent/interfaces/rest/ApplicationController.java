package com.fintech.aiagent.interfaces.rest;

import com.fintech.aiagent.domain.customer.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/application")
@RequiredArgsConstructor
public class ApplicationController {
    
    private final ApplicationService applicationService;

    @GetMapping("/status/{id}")
    public ResponseEntity<Map<String, Object>> getApplicationStatus(@PathVariable String id) {
        ApplicationStatus status = applicationService.getApplicationStatus(id);
        
        return ResponseEntity.ok(Map.of(
            "status", status.status(),
            "updateTime", status.updateTime().toString(),
            "progress", status.progress(),
            "estimatedCompletionTime", status.estimatedCompletionTime()
        ));
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadMaterials(
            @RequestParam("applicationId") String applicationId,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "documentType", required = false) String documentType) {
        
        UploadResult result = applicationService.uploadMaterials(applicationId, files, documentType);
        
        return ResponseEntity.ok(Map.of(
            "result", result.success() ? "success" : "failed",
            "missingFields", result.missingFields(),
            "uploadedFiles", result.uploadedFiles(),
            "message", result.message()
        ));
    }
} 