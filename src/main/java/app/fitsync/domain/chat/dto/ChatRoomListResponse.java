package app.fitsync.domain.chat.dto;

import app.fitsync.domain.chat.entity.ChatRoomType;
import java.time.LocalDateTime;
import java.util.List;

public record ChatRoomListResponse(
        Long roomId,
        ChatRoomType type,
        String name,
        List<Long> participantUserIds,
        String lastMessage,
        LocalDateTime lastMessageAt
) {
}
