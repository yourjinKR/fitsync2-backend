package app.fitsync.domain.chat.dto;

import java.time.LocalDateTime;

public record ChatNotificationResponse(
        ChatNotificationType type,
        Long roomId,
        String roomName,
        String senderName,
        String message,
        LocalDateTime createdAt
) {
}
