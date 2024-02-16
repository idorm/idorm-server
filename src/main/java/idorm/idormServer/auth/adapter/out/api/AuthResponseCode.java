package idorm.idormServer.auth.adapter.out.api;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import idorm.idormServer.common.exception.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthResponseCode implements BaseResponseCode {

	//성공
	MEMBER_LOGIN(OK, "회원 로그인 완료"),
	MEMBER_REFRESH(OK, "회원 액세스 토큰 재발급 완료"),
	MEMBER_LOGOUT(OK, "회원 로그아웃 완료"),

	//실패
	UNAUTHORIZED_LOGIN_INFO(UNAUTHORIZED, "아이디나 비밀번호가 잘못되었습니다."),
	UNAUTHORIZED_ACCESS_TOKEN(UNAUTHORIZED, "유효하지 않은 액세스 토큰 입니다."),
	UNAUTHORIZED_REFRESH_TOKEN(UNAUTHORIZED, "유효하지 않은 리프레시 토큰 입니다."),

	ACCESS_DENIED_ADMIN(FORBIDDEN, "관리자 접근 권한이 없습니다."),

	NOT_FOUND_TOKEN(NOT_FOUND, "토큰이 존재하지 않습니다.");

	private final HttpStatus status;
	private final String message;

	@Override
	public String getName() {
		return this.name();
	}
}
