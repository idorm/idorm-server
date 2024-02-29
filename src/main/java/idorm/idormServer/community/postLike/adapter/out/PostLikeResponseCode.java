package idorm.idormServer.community.postLike.adapter.out;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import idorm.idormServer.common.exception.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostLikeResponseCode implements BaseResponseCode {

	//성공
	LIKED_POST_MANY_FOUND(OK, "Post 로그인한 멤버가 공감한 게시글 조회 완료"),
	MEMBER_LIKED_POST(OK, "Post 게시글 공감 완료"),
	MEMBER_LIKED_POST_CANCELED(OK, "Post 게시글 공감 삭제 완료"),

	//실패
	NOT_FOUND_POSTLIKE(NOT_FOUND, "공감하지 않은 게시글 입니다."),

	DUPLICATED_POST_LIKE(CONFLICT, "공감한 게시글 입니다."),

	CANNOT_LIKED_SELF(CONFLICT, "본인 게시글은 공감할 수 없습니다."),
	CANNOT_LIKED_POST_BY_DELETED_MEMBER(CONFLICT, "탈퇴한 멤버의 게시글은 공감할 수 없습니다.");

	private final HttpStatus status;
	private final String message;

	@Override
	public String getName() {
		return this.name();
	}
}
