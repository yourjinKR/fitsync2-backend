package app.fitsync.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 응답")
public record UserResponse(
        @Schema(description = "사용자 ID", example = "1")
        Long id
) {

}
