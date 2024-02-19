package idorm.idormServer.common.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GlobalResponseCode implements BaseResponseCode {

	// 실패
	FILED_REQUIRED(BAD_REQUEST, "입력은 필수 입니다."),

	UNSUPPORTED_HTTP_METHOD(METHOD_NOT_ALLOWED, "지원하지 않는 메서드입니다."),

	SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버 에러가 발생했습니다.");

	private final HttpStatus status;
	private final String message;

	@Override
	public String getName() {
		return this.name();
	}
}