package app.fitsync.domain.user.dto;

public record UserRequest(
        String loginId,
        String password,
        String name
) {

}
