package app.fitsync.domain.user.mapper;

import app.fitsync.domain.user.dto.UserRequest;
import app.fitsync.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(UserRequest request, String encodedPassword) {
        return User.builder()
                .loginId(request.loginId())
                .password(encodedPassword)
                .name(request.name())
                .roleType(request.roleType())
                .email(request.email())
                .isSocial(request.isSocial())
                .socialProviderType(request.socialProviderType())
                .build();
    }
}
