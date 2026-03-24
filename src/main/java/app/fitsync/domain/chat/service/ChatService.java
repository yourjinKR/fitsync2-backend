package app.fitsync.domain.chat.service;

import app.fitsync.domain.chat.dto.ChatMessageResponse;
import app.fitsync.domain.chat.dto.ChatRoomListResponse;
import app.fitsync.domain.chat.dto.ChatRoomResponse;
import app.fitsync.domain.chat.dto.ChatSendRequest;
import app.fitsync.domain.chat.dto.DirectChatRoomCreateRequest;
import app.fitsync.domain.chat.dto.GroupChatRoomCreateRequest;
import app.fitsync.domain.chat.entity.ChatMessage;
import app.fitsync.domain.chat.entity.ChatMessageType;
import app.fitsync.domain.chat.entity.ChatRoom;
import app.fitsync.domain.chat.entity.ChatRoomParticipant;
import app.fitsync.domain.chat.entity.ChatRoomType;
import app.fitsync.domain.chat.exception.ChatErrorCode;
import app.fitsync.domain.chat.repository.ChatMessageRepository;
import app.fitsync.domain.chat.repository.ChatRoomParticipantRepository;
import app.fitsync.domain.chat.repository.ChatRoomRepository;
import app.fitsync.domain.user.entity.User;
import app.fitsync.domain.user.exception.UserErrorCode;
import app.fitsync.domain.user.repository.UserRepository;
import app.fitsync.global.exception.CommonErrorCode;
import app.fitsync.global.exception.RestApiException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService implements ChatServiceInterface {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomParticipantRepository chatRoomParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    @Transactional
    public ChatRoomResponse createDirectRoom(String loginId, DirectChatRoomCreateRequest request) {
        User me = findActiveUserByLoginId(loginId);
        User target = findActiveUserById(request.targetUserId());

        if (me.getId().equals(target.getId())) {
            throw new RestApiException(CommonErrorCode.INVALID_PARAMETER, "targetUserId");
        }

        ChatRoom existing = chatRoomRepository.findDirectRoomByUsers(ChatRoomType.DIRECT, me.getId(), target.getId())
                .orElse(null);
        if (existing != null) {
            return new ChatRoomResponse(existing.getId());
        }

        ChatRoom room = chatRoomRepository.save(ChatRoom.builder()
                .type(ChatRoomType.DIRECT)
                .name(null)
                .build());

        chatRoomParticipantRepository.save(ChatRoomParticipant.builder().chatRoom(room).user(me).build());
        chatRoomParticipantRepository.save(ChatRoomParticipant.builder().chatRoom(room).user(target).build());

        return new ChatRoomResponse(room.getId());
    }

    @Override
    @Transactional
    public ChatRoomResponse createGroupRoom(String loginId, GroupChatRoomCreateRequest request) {
        User me = findActiveUserByLoginId(loginId);

        Set<Long> participantIds = new LinkedHashSet<>();
        participantIds.add(me.getId());
        participantIds.addAll(request.participantUserIds());

        if (participantIds.size() < 3) {
            throw new RestApiException(CommonErrorCode.INVALID_PARAMETER, "participantUserIds");
        }

        ChatRoom room = chatRoomRepository.save(ChatRoom.builder()
                .type(ChatRoomType.GROUP)
                .name(request.name())
                .build());

        List<User> participants = new ArrayList<>();
        for (Long participantId : participantIds) {
            participants.add(findActiveUserById(participantId));
        }

        participants.forEach(user -> chatRoomParticipantRepository.save(
                ChatRoomParticipant.builder().chatRoom(room).user(user).build()
        ));

        return new ChatRoomResponse(room.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomListResponse> getMyRooms(String loginId) {
        User me = findActiveUserByLoginId(loginId);

        List<ChatRoomParticipant> myParticipations = chatRoomParticipantRepository.findByUserId(me.getId());

        List<ChatRoomListResponse> rooms = myParticipations.stream()
                .map(ChatRoomParticipant::getChatRoom)
                .distinct()
                .map(room -> {
                    List<Long> participantUserIds = chatRoomParticipantRepository.findByChatRoomId(room.getId())
                            .stream()
                            .map(participant -> participant.getUser().getId())
                            .toList();

                    ChatMessage lastMessage = chatMessageRepository.findTopByChatRoomIdOrderByCreatedAtDesc(room.getId())
                            .orElse(null);

                    String lastContent = lastMessage != null ? lastMessage.getContent() : null;
                    LocalDateTime lastMessageAt = lastMessage != null ? lastMessage.getCreatedAt() : room.getCreatedAt();

                    return new ChatRoomListResponse(
                            room.getId(),
                            room.getType(),
                            room.getName(),
                            participantUserIds,
                            lastContent,
                            lastMessageAt
                    );
                })
                .sorted(Comparator.comparing(ChatRoomListResponse::lastMessageAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .toList();

        return rooms;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChatMessageResponse> getRoomMessages(String loginId, Long roomId, Pageable pageable) {
        User me = findActiveUserByLoginId(loginId);
        validateRoomAccess(roomId, me.getId());

        return chatMessageRepository.findByChatRoomIdOrderByCreatedAtDesc(roomId, pageable)
                .map(this::toResponse);
    }

    @Override
    @Transactional
    public ChatMessageResponse sendMessage(String loginId, ChatSendRequest request) {
        User me = findActiveUserByLoginId(loginId);
        validateRoomAccess(request.roomId(), me.getId());

        if (request.type() == ChatMessageType.TEXT &&
                (request.content() == null || request.content().isBlank())) {
            throw new RestApiException(CommonErrorCode.INVALID_PARAMETER, "content");
        }

        ChatRoom room = chatRoomRepository.findById(request.roomId())
                .orElseThrow(() -> new RestApiException(ChatErrorCode.ROOM_NOT_FOUND, request.roomId()));

        ChatMessage saved = chatMessageRepository.save(ChatMessage.builder()
                .chatRoom(room)
                .sender(me)
                .type(request.type())
                .content(request.content())
                .build());

        ChatMessageResponse response = toResponse(saved);
        simpMessagingTemplate.convertAndSend("/sub/chat.rooms." + request.roomId(), response);
        return response;
    }

    private ChatMessageResponse toResponse(ChatMessage message) {
        return new ChatMessageResponse(
                message.getId(),
                message.getChatRoom().getId(),
                message.getSender().getId(),
                message.getSender().getName(),
                message.getType(),
                message.getContent(),
                message.getCreatedAt()
        );
    }

    private void validateRoomAccess(Long roomId, Long userId) {
        if (!chatRoomRepository.existsById(roomId)) {
            throw new RestApiException(ChatErrorCode.ROOM_NOT_FOUND, roomId);
        }
        boolean isParticipant = chatRoomParticipantRepository.existsByChatRoomIdAndUserId(roomId, userId);
        if (!isParticipant) {
            throw new RestApiException(ChatErrorCode.ROOM_ACCESS_DENIED, roomId);
        }
    }

    private User findActiveUserByLoginId(String loginId) {
        return userRepository.findByLoginIdAndHiddenIsFalse(loginId)
                .orElseThrow(() -> new RestApiException(UserErrorCode.NOT_FOUND_LOGIN_ID, loginId));
    }

    private User findActiveUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(ChatErrorCode.PARTICIPANT_NOT_FOUND, userId));

        if (user.isHidden()) {
            throw new RestApiException(ChatErrorCode.PARTICIPANT_NOT_FOUND, userId);
        }
        return user;
    }
}
