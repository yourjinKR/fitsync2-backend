package app.fitsync.domain.profile.dto;

import app.fitsync.domain.user.dto.UserHeaderInfoResponse;

public record UserWithProfileResponse(
        long userId,
        UserHeaderInfoResponse user,
        long profileId,
        UserProfileDetailResponse userProfile
) {
}
