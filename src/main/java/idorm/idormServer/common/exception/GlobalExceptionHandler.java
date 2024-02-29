package idorm.idormServer.common.exception;

import idorm.idormServer.common.response.ErrorResponse;
import idorm.idormServer.photo.adapter.out.PhotoResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.PAYLOAD_TOO_LARGE;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(BaseException exception) {
        log.error(exception.getCode().getMessage());

        ErrorResponse error = ErrorResponse.of(exception.getCode());
        return ResponseEntity.status(error.status()).body(error);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error("Exception Error", exception);

        ErrorResponse error = ErrorResponse.of(GlobalResponseCode.SERVER_ERROR);
        return ResponseEntity.status(error.status()).body(error);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException exception,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("HttpRequestMethodNotSupportedException", exception);

        ErrorResponse error = ErrorResponse.of(GlobalResponseCode.UNSUPPORTED_HTTP_METHOD);

        return ResponseEntity.status(error.status()).body(error);
    }

    @ExceptionHandler(value = {MaxUploadSizeExceededException.class})
    @ResponseStatus(PAYLOAD_TOO_LARGE)
    protected ResponseEntity<ErrorResponse> handleFileSizeLimitExceeded() {

        ErrorResponse error = ErrorResponse.of(PhotoResponseCode.EXCEED_FILE_SIZE);

        return ResponseEntity.status(error.status()).body(error);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("MethodArgumentNotValidException");
        ErrorResponse error = ErrorResponse.of(GlobalResponseCode.INVALID_MESSAGE_BODY);
        return ResponseEntity.status(error.status()).body(error);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        ErrorResponse error = ErrorResponse.of(GlobalResponseCode.INVALID_MESSAGE_BODY);
        return ResponseEntity.status(error.status()).body(error);
    }
}