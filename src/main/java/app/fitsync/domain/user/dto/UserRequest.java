package app.fitsync.domain.user.dto;

import app.fitsync.domain.user.entity.Gender;
import app.fitsync.domain.user.entity.SocialProviderType;
import app.fitsync.domain.user.entity.UserRoleType;

public record UserRequest(
        String loginId,
        String password,
        String name,
        Gender gender,
        BirthReqeust birth,
        UserRoleType roleType,
        String email,
        Boolean isSocial,
        SocialProviderType socialProviderType
) {

}
