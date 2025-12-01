package app.fitsync.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException {
    private final ErrorCode errorCode;
    private Object args;

    public RestApiException(ErrorCode errorCode, Object ...args) {
        this.errorCode = errorCode;
        this.args = args;
    }
}
