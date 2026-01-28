package app.fitsync.domain.user.exception;

import app.fitsync.global.exception.ErrorCode;
import java.text.MessageFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserException implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID와 일치하는 유저 정보를 찾지 못했습니다 ID : {0}"),
    DUPLICATE_LOGINID(HttpStatus.CONFLICT, "해당 ID와 일치하는 유저가 이미 존재합니다 login_id : {0}")
    ;

    private final HttpStatus httpStatus;
    private final String message;


    @Override
    public String format(Object... args) {
        return MessageFormat.format(message, args);
    }
}
