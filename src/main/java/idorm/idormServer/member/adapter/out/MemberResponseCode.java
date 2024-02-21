package idorm.idormServer.member.adapter.out;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import idorm.idormServer.common.exception.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberResponseCode implements BaseResponseCode {

	//성공
	MEMBER_FOUND(OK, "회원 단건 조회 완료"),
	NICKNAME_UPDATED(OK, "회원 닉네임 변경 완료"),
	PASSWORD_UPDATED(OK, "회원 비밀번호 변경 완료"),
	MEMBER_DELETED(OK, "회원 탈퇴 완료"),
	PROFILE_PHOTO_DELETED(OK, "회원 프로필 사진 삭제 완료"),
	MEMBER_LOGIN(OK, "회원 로그인 완료"),
	MEMBER_FCM_UPDATED(OK, "회원 FCM 업데이트 완료"),

	MEMBER_REGISTERED(CREATED, "회원 가입 완료"),
	PROFILE_PHOTO_SAVED(CREATED, "회원 프로필 사진 저장 완료"),

	//실패
	INVALID_PASSWORD_CHARACTER(HttpStatus.BAD_REQUEST, "올바른 형식의 비밀번호가 아닙니다."),
	INVALID_PASSWORD_LENGTH(HttpStatus.BAD_REQUEST, "비밀번호는 8~15자 이내여야 합니다."),
	INVALID_NICKNAME_CHARACTER(HttpStatus.BAD_REQUEST, "올바른 형식의 닉네임이 아닙니다."),
	INVALID_NICKNAME_LENGTH(HttpStatus.BAD_REQUEST, "닉네임은 2~8자만 이내여야 합니다."),

	UNAUTHORIZED_PASSWORD(UNAUTHORIZED, "올바른 비밀번호가 아닙니다."),
	UNAUTHORIZED_DELETED_MEMBER(UNAUTHORIZED, "탈퇴한 회원입니다. 로그아웃이 필요합니다."),

	NOT_FOUND_MEMBER(NOT_FOUND, "등록된 멤버가 없습니다."),
	NOT_FOUND_PROFILE_PHOTO(NOT_FOUND, "등록된 프로필 사진이 없습니다."),

	DUPLICATED_MEMBER(HttpStatus.CONFLICT, "등록된 멤버 입니다."),
	DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "등록된 닉네임 입니다."),
	DUPLICATED_SAME_NICKNAME(HttpStatus.CONFLICT, "기존의 닉네임과 동일합니다."),
	CANNOT_UPDATE_NICKNAME(HttpStatus.CONFLICT, "닉네임은 30일 간격으로 변경 가능합니다.");

	private final HttpStatus status;
	private final String message;

	@Override
	public String getName() {
		return this.name();
	}
}
