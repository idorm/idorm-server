package idorm.idormServer.matchingInfo.adapter.out;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import idorm.idormServer.common.exception.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchingInfoResponseCode implements BaseResponseCode {
	//성공
	MATCHINGINFO_FOUND(OK, "온보딩 단건 조회 완료"),
	MATCHINGINFO_SAVED(CREATED, "온보딩 저장 완료"),
	MATCHINGINFO_UPDATED(OK, "온보딩 수정 완료"),
	ISMATCHINGINFOPUBLIC_UPDATED(OK, "온보딩 공개 여부 수정 완료"),
	MATCHINGINFO_DELETED(OK, "온보딩 삭제 완료"),

	//실패
	INVALID_OPENKAKAOLINK_LENGTH(BAD_REQUEST, "오픈채팅 링크는 ~100자 이내여야 합니다."),
	INVALID_WAKEUPTIME_LENGTH(BAD_REQUEST, "기상 시간은 ~30자 이내여야 합니다."),
	INVALID_CLEENUP_STATUS_LENGTH(BAD_REQUEST, "정리정돈 상태는 ~30자 이내여야 합니다."),
	INVALID_SHOWERTIME_LENGTH(BAD_REQUEST, "샤워시간은 ~30자 이내여야 합니다."),
	INVALID_MBTI_LENGTH(BAD_REQUEST, "mbti는 ~4자 이내여야 합니다."),
	INVALID_MBTI_CHARACTER(BAD_REQUEST, "올바른 mbti 형식이 아닙니다."),
	INVALID_DORMCATEGORY_CHARACTER(BAD_REQUEST, "올바른 형식의 기숙사 분류가 아닙니다."),
	INVALID_JOIN_PERIOD_CHARACTER(BAD_REQUEST, "올바른 형식의 입사 기간이 아닙니다."),
	INVALID_GENDER_CHARACTER(BAD_REQUEST, "올바른 형식의 성별이 아닙니다."),
	INVALID_AGE_LENGTH(BAD_REQUEST, "나이는 20~100살 사이여야 합니다."),
	INVALID_WISHTEXT_LENGTH(BAD_REQUEST, "하고싶은 말은 ~150자 이내여야 합니다."),

	NOT_FOUND_MATCHINGINFO(NOT_FOUND, "등록된 온보딩 정보가 없습니다."),

	ILLEGAL_STATEMENT_MATCHINGINFO_NON_PUBLIC(CONFLICT, "본인 혹은 상대방이 비공개 상태의 온보딩 정보 입니다."),
	DUPLICATE_MATCHINGINFO(CONFLICT, "등록된 온보딩 정보가 존재합니다.");

	private final HttpStatus status;
	private final String message;

	@Override
	public String getName() {
		return this.name();
	}
}
