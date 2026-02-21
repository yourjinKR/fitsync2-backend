package app.fitsync.domain.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Created profile response")
public record UserProfileResponse(
        @Schema(description = "Profile ID", example = "10")
        long id
) {
}
