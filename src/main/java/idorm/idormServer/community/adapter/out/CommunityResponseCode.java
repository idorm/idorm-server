package idorm.idormServer.community.adapter.out;

import static org.springframework.http.HttpStatus.*;

import idorm.idormServer.common.exception.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommunityResponseCode implements BaseResponseCode {

  //성공
  POST_FILTER_FOUND(OK, "Post 기숙사 필터링 후 게시글 다건 조회 완료"),
  TOP_POST_MANY_FOUND(OK, "Post 인기 게시글 다건 조회 완료"),
  POST_FOUND(OK, "Post 단건 조회 완료"),
  POST_UPDATED(OK, "Post 수정 완료"),
  POST_MANY_FOUND(OK, "Post 내가 쓴 글 목록 조회 완료"),
  LIKED_POST_MANY_FOUND(OK, "Post 로그인한 멤버가 공감한 게시글 조회 완료"),
  Member_LIKED_POST(OK, "Post 게시글 공감 완료"),
  MEMBER_LIKED_POST_CANCELED(OK, "Post 게시글 공감 삭제 완료"),
  POST_DELETED(OK, "Post 게시글 삭제 완료"),
  COMMENT_DELETED(OK, "Comment 삭제 완료"),
  COMMENT_MANY_FOUND(OK, "Comment 로그인한 멤버가 작성한 모든 댓글 조회 완료"),

  POST_SAVED(CREATED, "Post 저장 완료"),
  COMMENT_SAVED(CREATED, "Comment 댓글 저장 완료"),

  //실패
  INVALID_CONTENT_LENGTH(BAD_REQUEST, "게시글 혹은 댓글 내용은 1~50자 이내여야 합니다."),
  INVALID_TITLE_LENGTH(BAD_REQUEST, "게시글 제목은 1~30자 이내여야 합니다."),

  ACCESS_DENIED_POST(FORBIDDEN, "게시글 업데이트 권한이 없습니다"),
  ACCESS_DENIED_UPDATE_POST(FORBIDDEN, "게시글 업데이트 권한이 없습니다"),
  ACCESS_DENIED_UPDATE_COMMENT(FORBIDDEN, "댓글 업데이트 권한이 없습니다"),

  NOT_FOUND_POSTPHOTO(NOT_FOUND, "등록된 게시글 사진이 없습니다."),
  NOT_FOUND_COMMENT(NOT_FOUND, "등록된 댓글이 없습니다."),
  NOT_FOUND_POSTLIKE(NOT_FOUND, "공감하지 않은 게시글 입니다."),
  ALREADY_DELETED_POST(NOT_FOUND, "삭제된 게시글 입니다."),
  ALREADY_DELETED_COMMENT(NOT_FOUND, "삭제된 댓글 입니다."),

  DUPLICATED_LIKED(CONFLICT, "공감한 게시글 입니다."),
  DUPLICATED_TEAM(CONFLICT, "등록된 팀이 존재합니다."),

  CANNOT_LIKED_SELF(CONFLICT, "본인 게시글은 공감할 수 없습니다."),
  CANNOT_LIKED_POST_BY_DELETED_MEMBER(CONFLICT, "탈퇴한 멤버의 게시글은 공감할 수 없습니다.");

  private final HttpStatus status;
  private final String message;

  @Override
  public String getName() {
    return this.name();
  }
}
