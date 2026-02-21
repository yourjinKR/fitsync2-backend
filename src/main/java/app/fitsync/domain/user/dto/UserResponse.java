package app.fitsync.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User response")
public record UserResponse(
        @Schema(description = "User ID", example = "1")
        Long id
) {

}
