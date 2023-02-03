package idorm.idormServer.exception;

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

import java.util.Objects;

import static idorm.idormServer.exception.ExceptionCode.*;
import static idorm.idormServer.exception.ExceptionCode.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { CustomException.class })
    protected ResponseEntity<DefaultExceptionResponseDto> handleCustomException(CustomException e) {
        log.error("[Error] {} - {}", e.getExceptionCode().name(), e.getCause());
        return DefaultExceptionResponseDto.exceptionResponse(e.getExceptionCode());
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

        if (responseMessage.contains("입력")) {
            responseCode = "FIELD_REQUIRED";
        } else if (responseMessage.contains("~")) {
            responseCode = exception.getFieldError().getField().toUpperCase().concat("_LENGTH_INVALID");
        } else if (responseMessage.contains("형식")) {
            responseCode = exception.getFieldError().getField().toUpperCase().concat("_FORMAT_INVALID");
        } else if (responseMessage.contains("양수")) {
            responseCode = exception.getFieldError().getField().toUpperCase().concat("_NEGATIVEORZERO_INVALID");
        } else {
            responseCode = exception.getFieldError().getField().toUpperCase();
        }

        log.error("[ERROR] {} - {}", responseMessage, exception.getCause());

        return ResponseEntity.status(status)
                .body(DefaultExceptionResponseDto.builder()
                        .responseCode(responseCode)
                        .responseMessage(responseMessage)
                        .build());
    }

    /**
     * 405 Method Not Allowed
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception,
                                                                         HttpHeaders headers,
                                                                         HttpStatus status,
                                                                         WebRequest request) {

        String responseCode = METHOD_NOT_ALLOWED.name();
        String responseMessage = METHOD_NOT_ALLOWED.getMessage();

        log.error("[ERROR] {} - {}", responseMessage, exception.getCause());

        return ResponseEntity.status(status)
                .body(DefaultExceptionResponseDto.builder()
                        .responseCode(responseCode)
                        .responseMessage(responseMessage)
                        .build());
    }

    /**
     * 413 Payload Too Large
     * 파일 용량은 최대 5MB 까지 받는다.
     */
    @ExceptionHandler(value = { MaxUploadSizeExceededException.class })
    @ResponseStatus(PAYLOAD_TOO_LARGE)
    protected ResponseEntity<DefaultExceptionResponseDto> handleFileSizeLimitExceeded() {

        log.error("[ERROR] {} - {}", FILE_SIZE_EXCEED.name(), FILE_SIZE_EXCEED.getMessage());
        return DefaultExceptionResponseDto.exceptionResponse(FILE_SIZE_EXCEED);
    }
}
