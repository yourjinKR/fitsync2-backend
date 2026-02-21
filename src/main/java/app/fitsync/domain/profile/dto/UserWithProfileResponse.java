package app.fitsync.domain.profile.dto;

import app.fitsync.domain.user.dto.UserHeaderInfoResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자+프로필 통합 응답")
public record UserWithProfileResponse(
        @Schema(description = "사용자 ID", example = "1")
        long userId,
        @Schema(description = "사용자 헤더 정보")
        UserHeaderInfoResponse user,
        @Schema(description = "프로필 ID", example = "10")
        long profileId,
        @Schema(description = "프로필 상세")
        UserProfileDetailResponse userProfile
) {
}
