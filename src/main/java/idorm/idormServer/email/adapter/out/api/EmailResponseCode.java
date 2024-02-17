package idorm.idormServer.email.adapter.out.api;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import idorm.idormServer.common.exception.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EmailResponseCode implements BaseResponseCode {

  //성공
  SEND_EMAIL(OK, "이메일 인증코드 전송 완료"),
  EMAIL_VERIFIED(OK, "이메일 인증코드 검증 완료"),
  SEND_REGISTERED_EMAIL(OK, "등록된 이메일 인증코드 전송 완료"),
  REGISTERED_EMAIL_VERIFIED(OK, "등록된 이메일 인증코드 검증 완료"),

  //실패
  INVALID_EMAIL_CHARACTER(BAD_REQUEST, "올바른 형식의 이메일이 아닙니다."),

  INVALID_VERIFICATION_CODE(UNAUTHORIZED, "올바르지 않은 코드 입니다."),
  UNAUTHORIZED_EMAIL(UNAUTHORIZED, "인증이 되지 않은 이메일 입니다."),
  EXPIRED_EMAIL_VERIFICATION_CODE(UNAUTHORIZED, "이메일 인증 유효시간이 초과되었습니다."),

  NOT_FOUND_EMAIL(NOT_FOUND, "등록된 이메일이 없습니다."),

  DUPLICATED_EMAIL(CONFLICT, "등록된 이메일 입니다."),

  EMAIL_SERVER_ERROR(INTERNAL_SERVER_ERROR, "이메일 전송 중 서버 에러가 발생했습니다.");

  private final HttpStatus status;
  private final String message;

  @Override
  public String getName() {
    return this.name();
  }
}
