package app.fitsync.domain.profile.exception;

import app.fitsync.global.exception.ErrorCode;
import java.text.MessageFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum InBodyException implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID와 일치하는 인바디 기록을 찾지 못했습니다. inBodyRecordId : {0}"),
    NOT_FOUND_PROFILE_ID(HttpStatus.NOT_FOUND, "해당 프로필 ID와 일치하는 인바디 기록을 찾지 못했습니다. profileId : {0}")
    ;

    private final HttpStatus httpStatus;
    private final String message;


    @Override
    public String format(Object... args) {
        return MessageFormat.format(message, args);
    }
}
