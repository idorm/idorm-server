package idorm.idormServer.community.comment.adapter.out;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import idorm.idormServer.common.exception.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentResponseCode implements BaseResponseCode {

	//성공
	COMMENT_DELETED(OK, "CommentDomain 삭제 완료"),
	COMMENT_MANY_FOUND(OK, "CommentDomain 로그인한 멤버가 작성한 모든 댓글 조회 완료"),

	COMMENT_SAVED(CREATED, "CommentDomain 댓글 저장 완료"),

	//실패
	INVALID_CONTENT_LENGTH(BAD_REQUEST, "댓글 내용은 1~50자 이내여야 합니다."),

	ACCESS_DENIED_COMMENT(FORBIDDEN, "댓글 작성 권한이 없습니다."),
	ACCESS_DENIED_UPDATE_COMMENT(FORBIDDEN, "댓글 업데이트 권한이 없습니다"),

	NOT_FOUND_COMMENT(NOT_FOUND, "등록된 댓글이 없습니다."),
	ALREADY_DELETED_COMMENT(NOT_FOUND, "삭제된 댓글 입니다."),
	;

	private final HttpStatus status;
	private final String message;

	@Override
	public String getName() {
		return this.name();
	}
}
