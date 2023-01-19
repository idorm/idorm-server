package idorm.idormServer.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.MethodNotSupportedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintDeclarationException;

import java.time.LocalDateTime;
import java.util.Objects;

import static idorm.idormServer.exceptions.ErrorCode.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ConstraintDeclarationException.class, DataIntegrityViolationException.class})
    protected ResponseEntity<ErrorResponse> handleDataException() {
        log.error("[DataException] {}", DUPLICATE_RESOURCE);
        return ErrorResponse.toResponseEntity(DUPLICATE_RESOURCE);
    }

    @ExceptionHandler(value = { CustomException.class })
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("[Exception] {}", e.getErrorCode());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    /**
     * 400 Bad Request |
     * 잘못된 응답 문법으로 인하여 서버가 요청하여 이해할 수 없습니다.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        String responseMessage = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();
        String responseCode = null;
        LocalDateTime timestamp = LocalDateTime.now();

        if (Objects.requireNonNull(responseMessage).contains("입력")) {
            responseCode = "FIELD_REQUIRED";
        } else if (responseMessage.contains("형식")) {
            responseCode = exception.getFieldError().getField().toUpperCase().concat("_FORMAT_INVALID");
        } else {
            responseCode = exception.getFieldError().getField().toUpperCase();
        }

        log.error("ERROR | " + responseMessage + " At " + timestamp + " | "
                + exception.getFieldError().getField() + " = " +
                ((Objects.requireNonNull(exception.getFieldError().getRejectedValue()).toString() == null) ?
                        "null" : exception.getFieldError().getRejectedValue().toString()));

        return ResponseEntity
                .status(status)
                .body(ErrorResponse.builder()
                        .error(status.name())
                        .code(responseCode)
                        .message(responseMessage)
                        .build()
                );
    }

    /**
     * 400 SizeLimitExceededException
     */
    @ExceptionHandler(value = { MaxUploadSizeExceededException.class })
    @ResponseStatus(PAYLOAD_TOO_LARGE)
    protected ResponseEntity<ErrorResponse> handleFileSizeLimitExceeded() {
        log.error("[Exception] Exception : {}", FILE_SIZE_EXCEEDED);
        return ErrorResponse.toResponseEntity(FILE_SIZE_EXCEEDED);
    }
}
