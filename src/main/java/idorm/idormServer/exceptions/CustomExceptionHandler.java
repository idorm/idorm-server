package idorm.idormServer.exceptions;

import idorm.idormServer.exceptions.http.ConflictException;
import idorm.idormServer.exceptions.http.InternalServerErrorException;
import idorm.idormServer.exceptions.http.NotFoundException;
import idorm.idormServer.exceptions.http.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 400 Bad request : 잘못된 응답 문법으로 인하여 서버가 요청을 이해할 수 없다.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        String responseMessage = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();
        String responseCode;
        LocalDateTime timestamp = LocalDateTime.now();

        if(Objects.requireNonNull(responseMessage).contains("입력")) {
            responseCode = "FIELD_REQUIRED";
        } else if ( responseMessage.contains("~")) {
            responseCode = exception.getFieldError().getField().toUpperCase().concat("_LENGTH_INVALID");
        } else if ( responseMessage.contains("형식")) {
            responseCode = exception.getFieldError().getField().toUpperCase().concat("_FORMAT_INVALID");
        } else {
            responseCode = exception.getFieldError().getField().toUpperCase();
        }

        log.error("ERROR | " + responseMessage + " At " + timestamp + " | "
                + exception.getFieldError().getField() + " = " +
                ((Objects.requireNonNull(exception.getFieldError().getRejectedValue()).toString() == null) ?
                        "null" : exception.getFieldError().getRejectedValue().toString()));

        return ResponseEntity.status(status).body(
                DefaultExceptionResponseDto.builder()
                        .responseCode(responseCode)
                        .responseMessage(responseMessage)
                        .build()
        );
    }

    /**
     * 401 Unauthorized : 미승인 또는 비인증이므로 클라이언트는 요청한 응답을 받기 위해서 반드시 스스로 인증해야된다.
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException exception) {
        String responseMessage = exception.getMessage();
        String responseCode = "UNAUTHORIZED";
        LocalDateTime timestamp = LocalDateTime.now();

        log.error("ERROR | " + responseMessage + " At " + timestamp + " | " + exception);

        return ResponseEntity.status(401).body(
                DefaultExceptionResponseDto.builder()
                        .responseCode(responseCode)
                        .responseMessage(responseMessage)
                        .build()
        );
    }

    /**
     * 404 Not Found |
     * 클라이언트는 콘텐츠에 접근할 권리를 가지고 있지 않습니다.
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException exception) {
        String responseMessage = exception.getMessage();
        String responseCode = "NOT_FOUND";
        LocalDateTime timestamp = LocalDateTime.now();

        log.error("ERROR | " + responseMessage + " At " + timestamp + " | " + exception);

        return ResponseEntity.status(404).body(
                DefaultExceptionResponseDto.builder()
                        .responseCode(responseCode)
                        .responseMessage(responseMessage)
                        .build()
        );
    }

    /**
     * 409 Conflict |
     * 요청이 현재 서버의 상태와 충돌됩니다.
     */
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    protected ResponseEntity<Object> handleConflictException(ConflictException exception) {
        String responseMessage = exception.getMessage();
        String responseCode = "CONFLICT";
        LocalDateTime timestamp = LocalDateTime.now();

        log.error("ERROR | " + responseMessage + " At " + timestamp + " | "
                + exception.getMessage() + " = " + exception.getCause());

        return ResponseEntity.status(409).body(
                DefaultExceptionResponseDto.builder()
                        .responseCode(responseCode)
                        .responseMessage(responseMessage)
                        .build()
        );
    }


    /**
     * 500 Internal Server Error |
     * 서버에 문제가 있음을 의미하지만 서버는 정확한 무제에 대해 더 구체적으로 설명할 수 없습니다.
     */
    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> handleInternalServerException(InternalServerErrorException exception) {
        String responseMessage = "서버 에러가 발생했습니다";
        String responseCode = "INTERNAL_SERVER_ERROR";
        LocalDateTime timestamp = LocalDateTime.now();

        log.error("ERROR | " + responseMessage + " At " + timestamp + " | "
                + exception.getMessage() + " = " + exception.getCause());

        return ResponseEntity.status(500).body(
                DefaultExceptionResponseDto.builder()
                        .responseCode(responseCode)
                        .responseMessage(responseMessage)
                        .build()
        );
    }
}
