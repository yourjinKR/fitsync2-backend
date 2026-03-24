package app.fitsync.domain.chat.dto;

import app.fitsync.domain.chat.entity.ChatMessageType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ChatSendRequest(
        @NotNull @Positive Long roomId,
        @NotNull ChatMessageType type,
        @Size(max = 2000) String content
) {
}
