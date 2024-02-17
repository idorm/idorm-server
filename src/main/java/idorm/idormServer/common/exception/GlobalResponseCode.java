package idorm.idormServer.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalResponseCode implements BaseResponseCode {

  // 실패
  FILED_REQUIRED(BAD_REQUEST, "입력은 필수 입니다."),
  ILLEGAL_ARGUMENT_ADMIN(BAD_REQUEST, "관리자는 해당 요청의 설정 대상이 될 수 없습니다."),
  ILLEGAL_ARGUMENT_SELF(BAD_REQUEST, "본인은 해당 요청의 설정 대상이 될 수 없습니다."),
  ILLEGAL_ARGUMENT_DATE_SET(BAD_REQUEST, "시작일자가 종료일자보다 빠르거나 같아야 합니다."),
  FILED_DATE_REQUIRED(BAD_REQUEST, "일자 입력은 필수 입니다."),

  UNSUPPORTED_HTTP_METHOD(METHOD_NOT_ALLOWED, "지원하지 않는 메서드입니다."),

  SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버 에러가 발생했습니다.");

  private final HttpStatus status;
  private final String message;

  @Override
  public String getName() {
    return this.name();
  }
}