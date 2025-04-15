package com.fintech.aiagent.application.dto;

import java.time.Instant;

public class NotificationResponse {
    private String notificationId;
    private String status;
    private String channel;
    private Instant sentTime;

    public NotificationResponse(String notificationId, String status, String channel, Instant sentTime) {
        this.notificationId = notificationId;
        this.status = status;
        this.channel = channel;
        this.sentTime = sentTime;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public String getStatus() {
        return status;
    }

    public String getChannel() {
        return channel;
    }

    public Instant getSentTime() {
        return sentTime;
    }
}