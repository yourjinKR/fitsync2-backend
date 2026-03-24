package app.fitsync.domain.chat.service;

import app.fitsync.domain.chat.dto.ChatNotificationResponse;
import app.fitsync.domain.chat.dto.ChatNotificationType;
import app.fitsync.domain.chat.entity.ChatRoom;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatNotificationService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public void notifyInvite(String loginId, ChatRoom room, String inviterName) {
        ChatNotificationResponse payload = new ChatNotificationResponse(
                ChatNotificationType.INVITE,
                room.getId(),
                room.getName(),
                inviterName,
                "새 그룹 채팅방에 초대되었습니다.",
                LocalDateTime.now()
        );
        simpMessagingTemplate.convertAndSendToUser(loginId, "/queue/notifications", payload);
    }

    public void notifyNewMessage(String loginId, ChatRoom room, String senderName, String message) {
        ChatNotificationResponse payload = new ChatNotificationResponse(
                ChatNotificationType.NEW_MESSAGE,
                room.getId(),
                room.getName(),
                senderName,
                message,
                LocalDateTime.now()
        );
        simpMessagingTemplate.convertAndSendToUser(loginId, "/queue/notifications", payload);
    }
}
