package app.fitsync.domain.user.dto;

import app.fitsync.global.DeleteType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UserDeleteRequest(
        @NotNull
        @Positive
        Long id,

        @NotNull
        DeleteType deleteType
) {
}
