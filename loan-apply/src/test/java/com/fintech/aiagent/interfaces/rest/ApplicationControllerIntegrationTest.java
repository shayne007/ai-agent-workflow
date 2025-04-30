package com.fintech.aiagent.interfaces.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSubmitApplication() throws Exception {
        // Create mock files
        MockMultipartFile file1 = new MockMultipartFile(
                "files",
                "document1.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "PDF content".getBytes()
        );
        
        MockMultipartFile file2 = new MockMultipartFile(
                "files",
                "document2.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "JPEG content".getBytes()
        );

        // Perform the request and validate
        mockMvc.perform(multipart("/api/v1/application/submit")
                .file(file1)
                .file(file2)
                .param("userId", "user123")
                .param("applicationType", "LOAN")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationId").exists())
                .andExpect(jsonPath("$.missingFields").exists())
                .andExpect(jsonPath("$.ocrResults").exists());
    }
}