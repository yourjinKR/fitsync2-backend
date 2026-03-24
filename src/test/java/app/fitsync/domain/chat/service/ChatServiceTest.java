package app.fitsync.domain.chat.service;

import app.fitsync.domain.chat.dto.ChatMessageResponse;
import app.fitsync.domain.chat.dto.ChatSendRequest;
import app.fitsync.domain.chat.dto.ChatRoomResponse;
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
import app.fitsync.domain.user.entity.UserRoleType;
import app.fitsync.domain.user.repository.UserRepository;
import app.fitsync.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatRoomParticipantRepository chatRoomParticipantRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @Mock
    private ChatNotificationService chatNotificationService;

    private ChatService newService() {
        return new ChatService(
                chatRoomRepository,
                chatRoomParticipantRepository,
                chatMessageRepository,
                userRepository,
                simpMessagingTemplate,
                chatNotificationService
        );
    }

    @Test
    @DisplayName("TS-CHAT-001: DIRECT 채팅방 생성 성공")
    void createDirectRoom_success() {
        ChatService chatService = newService();
        User me = user(1L, "me", "나");
        User target = user(2L, "target", "상대");
        ChatRoom savedRoom = ChatRoom.builder().id(11L).type(ChatRoomType.DIRECT).build();

        when(userRepository.findByLoginIdAndHiddenIsFalse("me")).thenReturn(Optional.of(me));
        when(userRepository.findById(2L)).thenReturn(Optional.of(target));
        when(chatRoomRepository.findDirectRoomByUsers(ChatRoomType.DIRECT, 1L, 2L)).thenReturn(Optional.empty());
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(savedRoom);

        ChatRoomResponse response = chatService.createDirectRoom("me", new DirectChatRoomCreateRequest(2L));

        assertThat(response.roomId()).isEqualTo(11L);
        verify(chatRoomRepository).save(any(ChatRoom.class));
        verify(chatRoomParticipantRepository, times(2)).save(any());
    }

    @Test
    @DisplayName("TS-CHAT-002: 동일 2인 DIRECT 채팅방 생성 시 기존 방을 재사용한다")
    void createDirectRoom_reusesExistingRoom() {
        ChatService chatService = newService();
        User me = user(1L, "me", "나");
        User target = user(2L, "target", "상대");
        ChatRoom existingRoom = ChatRoom.builder().id(99L).type(ChatRoomType.DIRECT).build();

        when(userRepository.findByLoginIdAndHiddenIsFalse("me")).thenReturn(Optional.of(me));
        when(userRepository.findById(2L)).thenReturn(Optional.of(target));
        when(chatRoomRepository.findDirectRoomByUsers(ChatRoomType.DIRECT, 1L, 2L))
                .thenReturn(Optional.of(existingRoom));

        ChatRoomResponse response = chatService.createDirectRoom("me", new DirectChatRoomCreateRequest(2L));

        assertThat(response.roomId()).isEqualTo(99L);
        verify(chatRoomRepository, never()).save(any(ChatRoom.class));
        verify(chatRoomParticipantRepository, never()).save(any());
    }

    @Test
    @DisplayName("TS-CHAT-003: GROUP 채팅방 생성 성공")
    void createGroupRoom_success() {
        ChatService chatService = newService();
        User me = user(1L, "me", "나");
        User user2 = user(2L, "user2", "둘");
        User user3 = user(3L, "user3", "셋");
        ChatRoom savedRoom = ChatRoom.builder().id(55L).type(ChatRoomType.GROUP).name("스터디").build();

        when(userRepository.findByLoginIdAndHiddenIsFalse("me")).thenReturn(Optional.of(me));
        when(userRepository.findById(1L)).thenReturn(Optional.of(me));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        when(userRepository.findById(3L)).thenReturn(Optional.of(user3));
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(savedRoom);

        ChatRoomResponse response = chatService.createGroupRoom(
                "me",
                new GroupChatRoomCreateRequest("스터디", List.of(2L, 3L))
        );

        assertThat(response.roomId()).isEqualTo(55L);
        verify(chatRoomRepository).save(any(ChatRoom.class));
        verify(chatRoomParticipantRepository, times(3)).save(any());
        verify(chatNotificationService).notifyInvite("user2", savedRoom, "나");
        verify(chatNotificationService).notifyInvite("user3", savedRoom, "나");
    }

    @Test
    @DisplayName("TS-CHAT-004: 비참여자의 채팅방 메시지 조회는 ROOM_ACCESS_DENIED")
    void getRoomMessages_deniedWhenNotParticipant() {
        ChatService chatService = newService();
        User me = user(1L, "me", "나");

        when(userRepository.findByLoginIdAndHiddenIsFalse("me")).thenReturn(Optional.of(me));
        when(chatRoomRepository.existsById(10L)).thenReturn(true);
        when(chatRoomParticipantRepository.existsByChatRoomIdAndUserId(10L, 1L)).thenReturn(false);

        assertThatThrownBy(() -> chatService.getRoomMessages("me", 10L, PageRequest.of(0, 10)))
                .isInstanceOf(RestApiException.class)
                .extracting("errorCode")
                .isEqualTo(ChatErrorCode.ROOM_ACCESS_DENIED);
    }

    @Test
    @DisplayName("TS-CHAT-006: 메시지 송신 성공 시 DB 저장 + 브로드캐스트")
    void sendMessage_success_persistsAndBroadcasts() {
        ChatService chatService = newService();
        User me = user(1L, "me", "나");
        ChatRoom room = ChatRoom.builder().id(20L).type(ChatRoomType.GROUP).name("스터디").build();
        ChatMessage saved = ChatMessage.builder()
                .id(101L)
                .chatRoom(room)
                .sender(me)
                .type(ChatMessageType.TEXT)
                .content("hello")
                .build();

        when(userRepository.findByLoginIdAndHiddenIsFalse("me")).thenReturn(Optional.of(me));
        when(chatRoomRepository.existsById(20L)).thenReturn(true);
        when(chatRoomParticipantRepository.existsByChatRoomIdAndUserId(20L, 1L)).thenReturn(true);
        when(chatRoomRepository.findById(20L)).thenReturn(Optional.of(room));
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(saved);
        when(chatRoomParticipantRepository.findByChatRoomId(20L)).thenReturn(List.of(
                ChatRoomParticipant.builder().chatRoom(room).user(me).build(),
                ChatRoomParticipant.builder().chatRoom(room).user(user(2L, "user2", "상대")).build()
        ));

        ChatMessageResponse response = chatService.sendMessage(
                "me",
                new ChatSendRequest(20L, ChatMessageType.TEXT, "hello")
        );

        assertThat(response.roomId()).isEqualTo(20L);
        assertThat(response.senderUserId()).isEqualTo(1L);
        assertThat(response.content()).isEqualTo("hello");

        ArgumentCaptor<ChatMessageResponse> captor = ArgumentCaptor.forClass(ChatMessageResponse.class);
        verify(simpMessagingTemplate).convertAndSend(org.mockito.ArgumentMatchers.eq("/sub/chat.rooms.20"), captor.capture());
        assertThat(captor.getValue().content()).isEqualTo("hello");
        verify(chatNotificationService).notifyNewMessage("user2", room, "나", "hello");
    }

    @Test
    @DisplayName("TS-CHAT-007: 비참여자 메시지 송신은 ROOM_ACCESS_DENIED")
    void sendMessage_deniedWhenNotParticipant() {
        ChatService chatService = newService();
        User me = user(1L, "me", "나");

        when(userRepository.findByLoginIdAndHiddenIsFalse("me")).thenReturn(Optional.of(me));
        when(chatRoomRepository.existsById(20L)).thenReturn(true);
        when(chatRoomParticipantRepository.existsByChatRoomIdAndUserId(20L, 1L)).thenReturn(false);

        assertThatThrownBy(() -> chatService.sendMessage("me", new ChatSendRequest(20L, ChatMessageType.TEXT, "hello")))
                .isInstanceOf(RestApiException.class)
                .extracting("errorCode")
                .isEqualTo(ChatErrorCode.ROOM_ACCESS_DENIED);

        verify(chatMessageRepository, never()).save(any(ChatMessage.class));
        verify(simpMessagingTemplate, never()).convertAndSend(anyString(), any(Object.class));
    }

    private User user(Long id, String loginId, String name) {
        return User.builder()
                .id(id)
                .loginId(loginId)
                .name(name)
                .password("pw")
                .email(loginId + "@fitsync.dev")
                .roleType(UserRoleType.MEMBER)
                .isSocial(false)
                .build();
    }
}
