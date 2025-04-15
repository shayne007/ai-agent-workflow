package aiagent.application.dto;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public class ApplicationSubmitRequest {
    private String userId;
    private String applicationType;
    private List<MultipartFile> files;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }
}