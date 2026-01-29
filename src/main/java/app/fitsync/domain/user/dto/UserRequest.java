package app.fitsync.domain.user.dto;

import app.fitsync.domain.user.entity.Gender;
import app.fitsync.domain.user.entity.SocialProviderType;
import app.fitsync.domain.user.entity.UserRoleType;
import java.time.LocalDateTime;

public record UserRequest(
        String loginId,
        String password,
        String name,
        Gender gender,
        LocalDateTime birth,
        UserRoleType roleType,
        String email,
        Boolean isSocial,
        SocialProviderType socialProviderType
) {

}
