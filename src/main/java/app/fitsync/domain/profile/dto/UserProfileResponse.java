package app.fitsync.domain.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "프로필 생성 응답")
public record UserProfileResponse(
        @Schema(description = "프로필 ID", example = "10")
        long id
) {
}
