package app.fitsync.domain.user.mapper;

import app.fitsync.domain.user.dto.UserRequest;
import app.fitsync.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(UserRequest request) {
        return User.builder()
                .loginId(request.loginId())
                .password(request.password())
                .name(request.name())
                .build();
    }
}
