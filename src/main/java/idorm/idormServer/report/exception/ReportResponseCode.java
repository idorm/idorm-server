package idorm.idormServer.report.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

import idorm.idormServer.common.exception.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReportResponseCode implements BaseResponseCode {
  //성공
  MEMBER_REPORTED(CREATED, "회원 신고 완료"),
  COMMUNITY_REPORTED(CREATED, "커뮤니티 신고 완료"),
  COMMENT_REPORTED(CREATED, "커뮤니티 댓글 신고 완료"),

  //실패
  INVALID_MEMBER_REPORT_TYPE(BAD_REQUEST, "올바른 회원 신고 형식이 아닙니다."),
  INVALID_COMMUNITY_REPORT_TYPE(BAD_REQUEST, "올바른 커뮤니티 신고 형식이 아닙니다."),
  INVALID_COMMENT_REPORT_TYPE(BAD_REQUEST, "올바른 커뮤니티 댓글 신고 형식이 아닙니다.");

  private final HttpStatus status;
  private final String message;

  @Override
  public String getName() {
    return this.name();
  }
}
