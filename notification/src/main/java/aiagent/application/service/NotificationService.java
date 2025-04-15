package aiagent.application.service;

import aiagent.application.dto.NotificationRequest;
import aiagent.application.dto.NotificationResponse;

public interface NotificationService {
    NotificationResponse sendNotification(NotificationRequest request);
}