package idorm.idormServer.common.exception;

import idorm.idormServer.common.response.ErrorResponse;
import idorm.idormServer.photo.adapter.out.api.PhotoResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(BaseException exception) {
        log.error(exception.getCode().getMessage());

        ErrorResponse error = ErrorResponse.of(exception.getCode());
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception exception){
        log.error("Exception Error", exception);

        ErrorResponse error = ErrorResponse.of(GlobalResponseCode.SERVER_ERROR);
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        log.error("HttpRequestMethodNotSupportedException", exception);

        ErrorResponse error = ErrorResponse.of(GlobalResponseCode.UNSUPPORTED_HTTP_METHOD);

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(value = { MaxUploadSizeExceededException.class })
    @ResponseStatus(PAYLOAD_TOO_LARGE)
    protected ResponseEntity<ErrorResponse> handleFileSizeLimitExceeded() {

        ErrorResponse error = ErrorResponse.of(PhotoResponseCode.EXCEED_FILE_SIZE);

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        log.error("MethodArgumentNotValidException");

        String errorMessage = exception.getFieldError().getDefaultMessage();

        ErrorResponse error = ErrorResponse.of(getResponseCodeFromErrorMessage(errorMessage), errorMessage);

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(value = { ConstraintViolationException.class })
    protected ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {

        ErrorResponse error;
        String errorCode = null;
        String errorMessage = null;
        if (exception.getConstraintViolations().stream().findFirst().isPresent()) {
            ConstraintViolation<?> constraintViolation = exception.getConstraintViolations().stream().findFirst().get();

            int propertyPathSize = constraintViolation.getPropertyPath().toString().split("\\.").length;

            errorCode = constraintViolation.getPropertyPath()
                    .toString()
                    .split("\\.")[propertyPathSize - 1]
                    .toUpperCase();

            errorMessage = constraintViolation.getMessageTemplate();

            error = ErrorResponse.of(getResponseCodeFromErrorMessage(errorMessage), errorMessage);
        } else {
            error = ErrorResponse.of(GlobalResponseCode.SERVER_ERROR);
        }

        return ResponseEntity.status(error.getStatus()).body(error);
    }


    private GlobalResponseCode getResponseCodeFromErrorMessage(String errorMessage) {

        GlobalResponseCode globalResponseCode;

        if (errorMessage.contains("입력")) {
            globalResponseCode = GlobalResponseCode.FILED_REQUIRED;
        } else if (errorMessage.contains("~")) {
            globalResponseCode = GlobalResponseCode.valueOf(errorMessage
                    .split("~")[0].toUpperCase() + "_LENGTH_INVALID");
        } else if (errorMessage.contains("형식")) {
            globalResponseCode = GlobalResponseCode.valueOf(errorMessage
                    .split("형식")[0].toUpperCase() + "_CHARACTER_INVALID");
        } else if (errorMessage.contains("양수")) {
            globalResponseCode = GlobalResponseCode.valueOf(errorMessage
                    .split("양수")[0].toUpperCase() + "_NEGATIVEORZERO_INVALID");
        } else {
            globalResponseCode = GlobalResponseCode.valueOf(errorMessage.toUpperCase());
        }

        return globalResponseCode;
    }

}
