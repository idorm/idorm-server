package idorm.idormServer.notification.adapter.out;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import idorm.idormServer.common.exception.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FcmResponseCode implements BaseResponseCode {

	//실패
	ILLEGAL_ARGUMENT_FCM_TOKEN(BAD_REQUEST, "유효하지 않은 FCM 토큰 입니다."),

	FIREBASE_SERER_ERROR(INTERNAL_SERVER_ERROR, "푸시알림 처리 중 서버 에러가 발생했습니다.");

	private final HttpStatus status;
	private final String message;

	@Override
	public String getName() {
		return this.name();
	}
}