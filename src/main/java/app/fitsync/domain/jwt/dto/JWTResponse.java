package app.fitsync.domain.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JWT token pair")
public record JWTResponse(
        @Schema(description = "Access token", example = "eyJhbGciOi...")
        String accessToken,
        @Schema(description = "Refresh token", example = "eyJhbGciOi...")
        String refreshToken) {
}

