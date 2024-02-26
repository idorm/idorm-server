package idorm.idormServer.community.post.adapter.out;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import idorm.idormServer.common.exception.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostResponseCode implements BaseResponseCode {

	//성공
	MAIN_POST_MANY_FOUND(OK, "PostDomain 기숙사 필터링 후 게시글 다건 조회 완료"),
	TOP_POST_MANY_FOUND(OK, "PostDomain 인기 게시글 다건 조회 완료"),
	MY_POST_MANY_FOUND(OK, "PostDomain 내가 쓴 글 목록 조회 완료"),
	POST_FOUND(OK, "PostDomain 단건 조회 완료"),
	POST_UPDATED(OK, "PostDomain 수정 완료"),
	POST_DELETED(OK, "PostDomain 게시글 삭제 완료"),

	POST_SAVED(CREATED, "PostDomain 저장 완료"),

	//실패
	INVALID_TITLE_LENGTH(BAD_REQUEST, "게시글 제목은 1~30자 이내여야 합니다."),
	INVALID_CONTENT_LENGTH(BAD_REQUEST, "게시글 혹은 댓글 내용은 1~50자 이내여야 합니다."),

	ACCESS_DENIED_POST(FORBIDDEN, "게시글 작성 권한이 없습니다"),
	ACCESS_DENIED_UPDATE_POST(FORBIDDEN, "게시글 업데이트 권한이 없습니다"),

	NOT_FOUND_POST(NOT_FOUND, "등록된 게시글이 없습니다."),
	ALREADY_DELETED_POST(NOT_FOUND, "삭제된 게시글 입니다.");

	private final HttpStatus status;
	private final String message;

	@Override
	public String getName() {
		return this.name();
	}
}
