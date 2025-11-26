package app.fitsync.domain.user.dto;

import app.fitsync.global.DeleteType;

public record UserDeleteRequest(
        Long id,
        DeleteType deleteType
) {
}
