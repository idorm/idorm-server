package idorm.idormServer.fcm.adapter.out;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

import idorm.idormServer.common.exception.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FcmResponseCode implements BaseResponseCode {
  //성공
  MEMBER_FCM_UPDATED(OK, "회원 FCM 업데이트 완료"),

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