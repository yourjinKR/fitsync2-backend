package app.fitsync.domain.user.dto;

import app.fitsync.domain.user.entity.Gender;

public record UserRequest(
        String loginId,
        String password,
        String name,
        Gender gender,
        BirthReqeust birth
) {

}
