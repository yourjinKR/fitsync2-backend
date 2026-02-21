package app.fitsync.domain.profile.dto;

import app.fitsync.domain.user.dto.UserHeaderInfoResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User and profile combined response")
public record UserWithProfileResponse(
        @Schema(description = "User ID", example = "1")
        long userId,
        @Schema(description = "User header info")
        UserHeaderInfoResponse user,
        @Schema(description = "Profile ID", example = "10")
        long profileId,
        @Schema(description = "Profile detail")
        UserProfileDetailResponse userProfile
) {
}
