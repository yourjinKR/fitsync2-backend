package app.fitsync.global.security;

import app.fitsync.domain.chat.repository.ChatRoomParticipantRepository;
import app.fitsync.domain.user.entity.User;
import app.fitsync.domain.user.repository.UserRepository;
import app.fitsync.global.util.JwtUtil;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatStompAuthChannelInterceptor implements ChannelInterceptor {

    private final UserRepository userRepository;
    private final ChatRoomParticipantRepository chatRoomParticipantRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null || accessor.getCommand() == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            authenticate(accessor);
            return message;
        }

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            Authentication authentication = (Authentication) accessor.getUser();
            if (authentication == null) {
                throw new AccessDeniedException("Unauthenticated websocket session");
            }

            validateRoomSubscription(authentication.getName(), accessor.getDestination());
        }

        return message;
    }

    private void authenticate(StompHeaderAccessor accessor) {
        String authorization = accessor.getFirstNativeHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new AccessDeniedException("Missing Authorization header");
        }

        String accessToken = authorization.substring(7);
        if (!JwtUtil.isValid(accessToken, true)) {
            throw new AccessDeniedException("Invalid access token");
        }

        String loginId = JwtUtil.getUsername(accessToken);
        String role = JwtUtil.getRole(accessToken);

        userRepository.findByLoginIdAndHiddenIsFalse(loginId)
                .orElseThrow(() -> new AccessDeniedException("User not found"));

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
        Authentication auth = new UsernamePasswordAuthenticationToken(loginId, null, authorities);
        accessor.setUser(auth);
    }

    private void validateRoomSubscription(String loginId, String destination) {
        if (destination == null || !destination.startsWith("/sub/chat.rooms.")) {
            return;
        }

        Long roomId = extractRoomId(destination);
        User user = userRepository.findByLoginIdAndHiddenIsFalse(loginId)
                .orElseThrow(() -> new AccessDeniedException("User not found"));

        boolean isParticipant = chatRoomParticipantRepository.existsByChatRoomIdAndUserId(roomId, user.getId());
        if (!isParticipant) {
            throw new AccessDeniedException("No room subscription permission");
        }
    }

    private Long extractRoomId(String destination) {
        String roomIdToken = destination.substring("/sub/chat.rooms.".length());
        try {
            return Long.parseLong(roomIdToken);
        } catch (NumberFormatException e) {
            throw new AccessDeniedException("Invalid room destination");
        }
    }
}
