package idorm.idormServer.matchingMate.adapter.out;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import idorm.idormServer.common.exception.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchingMateResponseCode implements BaseResponseCode {

	//성공
	LIKEDMEMBERS_FOUND(OK, "좋아요한 메이트들 조회 완료"),
	DISLIKEDMEMBERS_FOUND(OK, "싫어요한 메이트들 조회 완료"),
	LIKED_MEMBER_SAVED(OK, "좋아요한 메이트 추가 완료"),
	LIKED_MEMBER_DELETED(OK, "좋아요한 메이트 삭제 완료"),
	DISLIKED_MEMBER_SAVED(OK, "싫어요한 메이트 추가 완료"),
	DISLIKED_MEMBER_DELETED(OK, "싫어요한 메이트 삭제 완료"),
	MATCHING_MEMBERS_FOUND(OK, "기본 매칭 메이트들 조회 완료"),
	FILTERED_MATCHING_MEMBERS_FOUND(OK, "필터링 매칭 메이트들 조회 완료"),

	//실패
	INVALID_MATE_PREFERENCE_CHARACTER(BAD_REQUEST, "올바른 형식의 메이트 취향이 아닙니다."),

	NOT_FOUND_LIKEDMEMBER(NOT_FOUND, "등록된 좋아요한 멤버가 없습니다."),
	NOT_FOUND_DISLIKEDMEMBER(NOT_FOUND, "등록된 싫어요한 멤버가 없습니다."),

	ILLEGAL_STATEMENT_MATCHINGINFO_NON_PUBLIC(CONFLICT, "본인 혹은 상대방이 비공개 상태의 온보딩 정보 입니다."),
	DUPLICATED_LIKED_MEMBER(CONFLICT, "좋아요한 멤버 입니다."),
	DUPLICATED_DISLIKED_MEMBER(CONFLICT, "싫어요한 멤버 입니다.");

	private final HttpStatus status;
	private final String message;

	@Override
	public String getName() {
		return this.name();
	}
}
