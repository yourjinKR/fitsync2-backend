package app.fitsync.domain.jwt.dto;

public record JWTResponse(
        String accessToken,
        String refreshToken) {
}

