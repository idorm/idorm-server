package idorm.idormServer.photo.adapter.out.api;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import idorm.idormServer.common.exception.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PhotoResponseCode implements BaseResponseCode {

  //실패
  NOT_FOUND_FILE(NOT_FOUND, "파일이 존재하지 않습니다."),

  EXCEED_FILE_SIZE(PAYLOAD_TOO_LARGE, "파일 용량이 초과되었습니다."),
  EXCEED_FILE_COUNT(PAYLOAD_TOO_LARGE, "파일 개수가 초과되었습니다."),

  UNSUPPORTED_FILE_TYPE(UNSUPPORTED_MEDIA_TYPE, "파일 형식은 '.jpg', '.jpeg', '.png' 만 가능합니다."),

  S3_SERVER_ERROR(INTERNAL_SERVER_ERROR, "이미지 파일 저장 중 서버 에러가 발생했습니다.");

  private final HttpStatus status;
  private final String message;

  @Override
  public String getName(){
    return this.name();
  }
}