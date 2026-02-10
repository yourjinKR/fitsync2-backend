package app.fitsync.domain.user.dto;

public record LoginRequest(
        String loginId,
        String password
) {
}
