package app.fitsync.global.security;

import app.fitsync.domain.chat.repository.ChatRoomParticipantRepository;
import app.fitsync.domain.user.entity.User;
import app.fitsync.domain.user.entity.UserRoleType;
import app.fitsync.domain.user.repository.UserRepository;
import app.fitsync.global.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatStompAuthChannelInterceptorTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChatRoomParticipantRepository chatRoomParticipantRepository;

    @Test
    @DisplayName("TS-CHAT-005: STOMP CONNECT에서 JWT 누락 시 연결을 거부한다")
    void connect_withoutAuthorizationHeader_denied() {
        ChatStompAuthChannelInterceptor interceptor =
                new ChatStompAuthChannelInterceptor(userRepository, chatRoomParticipantRepository);

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        Message<byte[]> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());

        assertThatThrownBy(() -> interceptor.preSend(message, null))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("TS-CHAT-005: STOMP CONNECT에서 JWT 무효 시 연결을 거부한다")
    void connect_withInvalidAuthorizationHeader_denied() {
        ChatStompAuthChannelInterceptor interceptor =
                new ChatStompAuthChannelInterceptor(userRepository, chatRoomParticipantRepository);

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        accessor.setNativeHeader("Authorization", "Bearer invalid-token");
        Message<byte[]> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());

        assertThatThrownBy(() -> interceptor.preSend(message, null))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("유효한 JWT CONNECT는 인증 컨텍스트를 설정한다")
    void connect_withValidJwt_setsPrincipal() {
        ChatStompAuthChannelInterceptor interceptor =
                new ChatStompAuthChannelInterceptor(userRepository, chatRoomParticipantRepository);

        User me = User.builder()
                .id(1L)
                .loginId("me")
                .name("나")
                .password("pw")
                .email("me@fitsync.dev")
                .roleType(UserRoleType.MEMBER)
                .isSocial(false)
                .build();

        String accessToken = JwtUtil.createJWT("me", "MEMBER", true);

        when(userRepository.findByLoginIdAndHiddenIsFalse("me")).thenReturn(Optional.of(me));

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        accessor.setNativeHeader("Authorization", "Bearer " + accessToken);
        accessor.setLeaveMutable(true);
        Message<byte[]> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());

        Message<?> result = interceptor.preSend(message, null);

        StompHeaderAccessor wrapped = StompHeaderAccessor.wrap(result);
        assertThat(wrapped.getUser()).isNotNull();
        assertThat(wrapped.getUser().getName()).isEqualTo("me");
    }
}
