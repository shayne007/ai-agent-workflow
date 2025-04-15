package aiagent.application.service.impl;



import aiagent.application.dto.NotificationRequest;
import aiagent.application.dto.NotificationResponse;
import aiagent.application.service.NotificationService;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Override
    public NotificationResponse sendNotification(NotificationRequest request) {
        // 在实际应用中，这里会调用消息队列或第三方通知服务
        // 这里简单模拟发送通知的过程
        
        String notificationId = UUID.randomUUID().toString();
        String status = "sent";
        String channel = request.getChannel();
        
        // 记录日志
        System.out.println("发送通知: " + request.getMessage() + " 到用户: " + request.getUserId() + 
                " 通过渠道: " + channel);
        
        // 返回通知结果
        return new NotificationResponse(
                notificationId,
                status,
                channel,
                Instant.now()
        );
    }
}