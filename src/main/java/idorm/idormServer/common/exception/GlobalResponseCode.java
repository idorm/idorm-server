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
	INVALID_TARGET_SELF(BAD_REQUEST, "본인은 해당 요청의 설정 대상이 될 수 없습니다."),
	INVALID_MESSAGE_BODY(BAD_REQUEST, "요청 바디의 형식이 잘못되었습니다."),

	UNSUPPORTED_HTTP_METHOD(METHOD_NOT_ALLOWED, "지원하지 않는 메서드입니다."),

	SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버 에러가 발생했습니다.");

	private final HttpStatus status;
	private final String message;

	@Override
	public String getName() {
		return this.name();
	}
}