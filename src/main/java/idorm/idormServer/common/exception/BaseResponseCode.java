package idorm.idormServer.common.exception;

import org.springframework.http.HttpStatus;

public interface BaseResponseCode {
	String getMessage();
	String getName();
	HttpStatus getStatus();
}