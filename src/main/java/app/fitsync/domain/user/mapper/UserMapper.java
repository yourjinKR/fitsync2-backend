package app.fitsync.domain.user.mapper;

import app.fitsync.domain.user.dto.BirthReqeust;
import app.fitsync.domain.user.dto.UserRequest;
import app.fitsync.domain.user.entity.BirthDate;
import app.fitsync.domain.user.entity.User;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(UserRequest request, String encodedPassword) {
        return User.builder()
                .loginId(request.loginId())
                .password(encodedPassword)
                .name(request.name())
                .gender(request.gender())
                .birth(new BirthDate(request.birth()))
                .roleType(request.roleType())
                .email(request.email())
                .isSocial(request.isSocial())
                .socialProviderType(request.socialProviderType())
                .build();
    }

    public static BirthDate toBirthDate(BirthReqeust reqeust) {
        if (reqeust == null) { return BirthDate.EMPTY; }

        LocalDateTime birth = LocalDateTime.of(
                reqeust.year(),
                reqeust.month(),
                reqeust.dayOfMonth(),
                0,
                0
        );

        return new BirthDate(birth);
    }
}
