package app.fitsync.domain.chat.controller;

import app.fitsync.domain.chat.dto.ChatMessageResponse;
import app.fitsync.domain.chat.dto.ChatRoomListResponse;
import app.fitsync.domain.chat.dto.ChatRoomInviteRequest;
import app.fitsync.domain.chat.dto.ChatRoomResponse;
import app.fitsync.domain.chat.dto.DirectChatRoomCreateRequest;
import app.fitsync.domain.chat.dto.GroupChatRoomCreateRequest;
import app.fitsync.domain.chat.service.ChatServiceInterface;
import jakarta.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatServiceInterface chatService;

    @PostMapping("/api/chat/rooms/direct")
    public ResponseEntity<ChatRoomResponse> createDirectRoom(
            @Valid @RequestBody DirectChatRoomCreateRequest request,
            Principal principal
    ) {
        ChatRoomResponse response = chatService.createDirectRoom(principal.getName(), request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .replacePath("/api/chat/rooms/{id}")
                .buildAndExpand(response.roomId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/api/chat/rooms/group")
    public ResponseEntity<ChatRoomResponse> createGroupRoom(
            @Valid @RequestBody GroupChatRoomCreateRequest request,
            Principal principal
    ) {
        ChatRoomResponse response = chatService.createGroupRoom(principal.getName(), request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .replacePath("/api/chat/rooms/{id}")
                .buildAndExpand(response.roomId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/api/chat/rooms")
    public ResponseEntity<List<ChatRoomListResponse>> getMyRooms(Principal principal) {
        return ResponseEntity.ok(chatService.getMyRooms(principal.getName()));
    }

    @GetMapping("/api/chat/rooms/{roomId}/messages")
    public ResponseEntity<Page<ChatMessageResponse>> getRoomMessages(
            @PathVariable Long roomId,
            @PageableDefault(size = 30, sort = "createdAt", direction = Direction.DESC) Pageable pageable,
            Principal principal
    ) {
        return ResponseEntity.ok(chatService.getRoomMessages(principal.getName(), roomId, pageable));
    }

    @PostMapping("/api/chat/rooms/{roomId}/read")
    public ResponseEntity<Void> markRoomAsRead(
            @PathVariable Long roomId,
            Principal principal
    ) {
        chatService.markRoomAsRead(principal.getName(), roomId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/chat/rooms/{roomId}/invite")
    public ResponseEntity<ChatRoomResponse> inviteToGroupRoom(
            @PathVariable Long roomId,
            @Valid @RequestBody ChatRoomInviteRequest request,
            Principal principal
    ) {
        return ResponseEntity.ok(chatService.inviteToGroupRoom(principal.getName(), roomId, request));
    }
}
