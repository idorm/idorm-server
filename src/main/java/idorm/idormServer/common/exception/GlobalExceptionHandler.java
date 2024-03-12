package idorm.idormServer.common.exception;

import static org.springframework.http.HttpStatus.*;

import java.util.Objects;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

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

import idorm.idormServer.common.response.ErrorResponse;
import idorm.idormServer.photo.adapter.out.PhotoResponseCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BaseException.class)
	protected ResponseEntity<ErrorResponse> handleCustomException(BaseException exception) {
		ErrorResponse error = ErrorResponse.of(exception.getCode());
		return ResponseEntity.status(error.status()).body(error);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleException(Exception exception) {
		ErrorResponse error = ErrorResponse.of(GlobalResponseCode.SERVER_ERROR);
		return ResponseEntity.status(error.status()).body(error);
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
		HttpRequestMethodNotSupportedException exception,
		HttpHeaders headers, HttpStatus status, WebRequest request) {
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
		int index = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage().indexOf(",");
		String responseMessage = Objects.requireNonNull(exception.getFieldError())
			.getDefaultMessage()
			.substring(index + 2);
		String responseCode = determineErrorMessage(
			Objects.requireNonNull(exception.getFieldError()).getDefaultMessage());
		ErrorResponse error = ErrorResponse.of(HttpStatus.BAD_REQUEST, responseMessage, responseCode);
		return ResponseEntity.status(error.status()).body(error);
	}

	@ExceptionHandler(value = {ConstraintViolationException.class})
	protected ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
		ConstraintViolation<?> constraintViolation = exception.getConstraintViolations().stream().findFirst().get();
		int index = constraintViolation.getMessageTemplate().indexOf(",");
		String responseMessage = constraintViolation.getMessageTemplate().substring(index + 2);
		String responseCode = determineErrorMessage(responseMessage);
		ErrorResponse error = ErrorResponse.of(HttpStatus.BAD_REQUEST, responseMessage, responseCode);
		return ResponseEntity.status(error.status()).body(error);
	}

	private static String determineErrorMessage(String message) {
		int index = message.indexOf(" ");
		if (message.contains("공백") || message.contains("입력")) {
			return GlobalResponseCode.FILED_REQUIRED.getName();
		} else if (message.contains("~")) {
			return "INVALID_" + message.substring(0, index).toUpperCase() + "_LENGTH";
		} else if (message.contains("형식")) {
			return "INVALID_" + message.substring(0, index).toUpperCase() + "_CHARACTER";
		} else if (message.contains("양수")) {
			return "INVALID_" + message.substring(0, index).toUpperCase() + "_NEGATIVEORZERO";
		}
		return GlobalResponseCode.INVALID_MESSAGE_BODY.getName();
	}
}