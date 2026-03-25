package app.fitsync.domain.chat.dto;

import app.fitsync.domain.chat.entity.ChatMessageType;
import java.time.LocalDateTime;

public record ChatMessageResponse(
        Long id,
        Long roomId,
        Long senderUserId,
        String senderName,
        ChatMessageType type,
        String content,
        LocalDateTime createdAt
) {
}
