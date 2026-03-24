package app.fitsync.domain.chat.service;

import app.fitsync.domain.chat.dto.ChatMessageResponse;
import app.fitsync.domain.chat.dto.ChatRoomListResponse;
import app.fitsync.domain.chat.dto.ChatRoomInviteRequest;
import app.fitsync.domain.chat.dto.ChatRoomResponse;
import app.fitsync.domain.chat.dto.ChatSendRequest;
import app.fitsync.domain.chat.dto.DirectChatRoomCreateRequest;
import app.fitsync.domain.chat.dto.GroupChatRoomCreateRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatServiceInterface {

    ChatRoomResponse createDirectRoom(String loginId, DirectChatRoomCreateRequest request);

    ChatRoomResponse createGroupRoom(String loginId, GroupChatRoomCreateRequest request);

    List<ChatRoomListResponse> getMyRooms(String loginId);

    Page<ChatMessageResponse> getRoomMessages(String loginId, Long roomId, Pageable pageable);

    ChatMessageResponse sendMessage(String loginId, ChatSendRequest request);

    void markRoomAsRead(String loginId, Long roomId);

    ChatRoomResponse inviteToGroupRoom(String loginId, Long roomId, ChatRoomInviteRequest request);
}
