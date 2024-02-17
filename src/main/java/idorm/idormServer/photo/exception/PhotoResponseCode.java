package idorm.idormServer.photo.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import idorm.idormServer.common.exception.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PhotoResponseCode implements BaseResponseCode {

  //실패
  EXCEED_FILE_SIZE(PAYLOAD_TOO_LARGE, "파일 용량이 초과되었습니다."),
  EXCEED_FILE_COUNT(PAYLOAD_TOO_LARGE, "파일 개수가 초과되었습니다."),

  UNSUPPORTED_FILE_TYPE(UNSUPPORTED_MEDIA_TYPE, "파일 형식은 '.jpg', '.jpeg', '.png' 만 가능합니다.");

  private final HttpStatus status;
  private final String message;

  @Override
  public String getName(){
    return this.name();
  }
}