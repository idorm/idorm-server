package idorm.idormServer.matchingMate.exception;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import idorm.idormServer.common.exception.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MatchingMateResponseCode implements BaseResponseCode {

  //성공
  LIKEDMEMBERS_FOUND(OK, "Matching 좋아요한 회원 다건 조회 완료"),
  DISLIKEDMEMBERS_FOUND(OK, "Matching 싫어요한 회원 다건 조회 완료"),
  DISLIKED_MEMBER_SAVED(OK, "Matching 싫어요한 회원 추가 완료"),
  DISLIKED_MEMBER_DELETED(OK, "Matching 싫어요한 회원 삭제 완료"),
  LIKED_MEMBER_SAVED(OK, "Matching 좋아요 회원 추가 완료"),
  LIKED_MEMBER_DELETED(OK, "Matching 좋아요 회원 삭제 완료"),
  MATCHING_MEMBERS_FOUND(OK, "Matching 매칭 회원 조회 완료"),
  FILTERED_MATCHING_MEMBERS_FOUND(OK, "Matching 필터링 후 매칭 회원 조회 완료"),

  //실패
  NOT_FOUD_LIKEDMEMBER(NOT_FOUND, "등록된 좋아요한 멤버가 없습니다."),
  NOT_FOUD_DISLIKEDMEMBER(NOT_FOUND, "등록된 싫어요한 멤버가 없습니다."),
  DUPLICATED_LIKED_MEMBER(CONFLICT, "좋아요한 멤버 입니다."),
  DUPLICATED_DISLIKED_MEMBER(CONFLICT, "싫어요한 멤버 입니다.");

  private final HttpStatus status;
  private final String message;

  @Override
  public String getName() {
    return this.name();
  }
}
