package app.fitsync.domain.chat.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record ChatRoomInviteRequest(
        @NotEmpty List<Long> participantUserIds
) {
}
