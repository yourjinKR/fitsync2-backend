package app.fitsync.domain.profile.exception;

import app.fitsync.global.exception.ErrorCode;
import java.text.MessageFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserProfileException implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID와 일치하는 유저 프로필을 찾지 못했습니다 USER_ID : {0}"),
    DUPLICATE(HttpStatus.CONFLICT, "이미 해당 유저의 프로필이 존재합니다 ID : {0}")
    ;

    private final HttpStatus httpStatus;
    private final String message;


    @Override
    public String format(Object... args) {
        return MessageFormat.format(message, args);
    }
}
