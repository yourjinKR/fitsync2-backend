package app.fitsync.domain.user.mapper;

import app.fitsync.domain.user.dto.BirthReqeust;
import app.fitsync.domain.user.dto.UserRequest;
import app.fitsync.domain.user.entity.BirthDate;
import app.fitsync.domain.user.entity.User;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(UserRequest request) {
        return User.builder()
                .loginId(request.loginId())
                .password(request.password())
                .name(request.name())
                .gender(request.gender())
                .birth(toBirthDate(request.birth()))
                .build();
    }

    static BirthDate toBirthDate(BirthReqeust reqeust) {
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
