package app.fitsync.domain.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JWT 토큰 쌍")
public record JWTResponse(
        @Schema(description = "액세스 토큰", example = "eyJhbGciOi...")
        String accessToken,
        @Schema(description = "리프레시 토큰", example = "eyJhbGciOi...")
        String refreshToken) {
}

