package idorm.idormServer.report.adapter.out;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import idorm.idormServer.common.exception.BaseResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportResponseCode implements BaseResponseCode {
	//성공
	MEMBER_REPORTED(CREATED, "회원 신고 완료"),
	POST_REPORTED(CREATED, "게시글 신고 완료"),
	COMMENT_REPORTED(CREATED, "커뮤니티 댓글 신고 완료"),

	//실패
	INVALID_REPORT_TYPE(BAD_REQUEST, "올바른 신고 형식이 아닙니다."),
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
