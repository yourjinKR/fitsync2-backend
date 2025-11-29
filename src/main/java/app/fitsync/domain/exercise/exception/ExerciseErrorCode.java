package app.fitsync.domain.exercise.exception;

import app.fitsync.global.exception.ErrorCode;
import java.text.MessageFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExerciseErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID와 일치하는 운동 정보를 찾지 못했습니다 ID : {0}"),
    BODY_DETAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID와 일치하는 세부 부위를 찾지 못했습니다 ID : {0}"),
    ;

    private final HttpStatus httpStatus;
    private final String message;


    @Override
    public String format(Object... args) {
        return MessageFormat.format(message, args);
    }
}
