package app.fitsync.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class RestApiException extends RuntimeException {
    private final ErrorCode errorCode;
}
