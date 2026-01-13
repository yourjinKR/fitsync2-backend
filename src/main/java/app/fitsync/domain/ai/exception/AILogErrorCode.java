package app.fitsync.domain.ai.exception;

import app.fitsync.global.exception.ErrorCode;
import java.text.MessageFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AILogErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID와 일치하는 요청 기록을 찾지 못했습니다 ID : {0}"),
    UUID_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 UUID와 일치하는 요청기록 찾지 못했습니다 UUID : {0}"),
    ;

    private final HttpStatus httpStatus;
    private final String message;


    @Override
    public String format(Object... args) {
        return MessageFormat.format(message, args);
    }

}
