package app.fitsync.domain.chat.controller;

import app.fitsync.domain.chat.dto.ChatSendRequest;
import app.fitsync.domain.chat.service.ChatServiceInterface;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final ChatServiceInterface chatService;

    @MessageMapping("/chat.send")
    public void sendMessage(@Valid ChatSendRequest request, Principal principal) {
        chatService.sendMessage(principal.getName(), request);
    }
}
