package idorm.idormServer.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final Exception exception;
    private final ExceptionCode exceptionCode;
}
