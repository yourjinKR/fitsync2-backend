package app.fitsync.domain.routine.exception;

import app.fitsync.global.exception.ErrorCode;
import java.text.MessageFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RoutineErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID와 일치하는 운동 루틴을 찾지 못했습니다 ID : {0}"),
    EXERCISE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID와 일치하는 운동 세트를 찾지 못했습니다 ID : {0}"),
    SET_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID와 일치하는 기록 세트를 찾지 못했습니다 ID : {0}"),
    ;

    private final HttpStatus httpStatus;
    private final String message;


    @Override
    public String format(Object... args) {
        return MessageFormat.format(message, args);
    }
}
