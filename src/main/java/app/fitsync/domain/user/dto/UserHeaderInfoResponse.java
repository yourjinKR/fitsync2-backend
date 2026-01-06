package app.fitsync.domain.user.dto;

public record UserHeaderInfoResponse(
        String name,
        Long age,
        boolean hidden
) {
}
