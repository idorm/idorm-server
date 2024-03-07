package idorm.idormServer.report.application.port.in.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import idorm.idormServer.community.comment.entity.Comment;
import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.member.entity.Member;
import idorm.idormServer.report.entity.CommentReport;
import idorm.idormServer.report.entity.CommunityReason;
import idorm.idormServer.report.entity.MemberReason;
import idorm.idormServer.report.entity.MemberReport;
import idorm.idormServer.report.entity.PostReport;
import idorm.idormServer.report.entity.Report;
import io.swagger.v3.oas.annotations.media.Schema;

public record ReportRequest(
	@Schema(required = true, allowableValues = "MEMBER, POST, COMMENT")
	@NotBlank(message = "모든 필수 항목을 입력해주세요.")
	String reportType,

	@Schema(required = true, description = "신고 회원 또는 신고 게시글 또는 신고 댓글 식별자", example = "1")
	@NotNull(message = "모든 필수 항목을 입력해주세요.")
	@Positive(message = "targetId , 식별자는 양수만 가능합니다.")
	Long targetId,

	@Schema(required = true,
		description = "사유 종류: 회원('NICKNAME', 'PROFILE_PHOTO', 'MATCHINGINFO', 'ETC'), " +
			"커뮤니티('PLASTER', 'ADVERTISING_SPAM', 'OBSCENE_MATERIAL', 'ABUSE', 'FALSE_INFORMATION', 'ETC')",
		allowableValues = "NICKNAME(닉네임), PROFILE_PHOTO(프로필 사진), MATCHINGINFO(온보딩 정보), " +
			"PLASTER(도배), ADVERTISING_SPAM(광고 / 스팸), OBSCENE_MATERIAL(음란물 / 선정성), " +
			"ABUSE(욕설), FALSE_INFORMATION(사칭 / 허위 정보), ETC(기타)",
		example = "NICKNAME")
	@NotBlank(message = "모든 필수 항목을 입력해주세요.")
	String reasonType,

	@Schema(description = "사유", example = "사유")
	@Size(max = 200, message = "reaseon , 사유는 0~200자 이내여야 합니다.")
	String reason
) {

	public Report toMemberReportDomain(final Member reporterMember, final Member reportedMember) {
		return new MemberReport(reporterMember, reportedMember, MemberReason.from(this.reasonType), reason);
	}

	public Report toPostReportDomain(final Member reporterMember, final Post reportedPost) {
		return new PostReport(reporterMember, reportedPost.getMember(), reportedPost, CommunityReason.from(reasonType),
			reason);
	}

	public Report toCommentReportDomain(final Member reporterMember, final Comment reportedComment) {
		return new CommentReport(reporterMember, reportedComment.getMember(), reportedComment,
			CommunityReason.from(reasonType), reason);
	}
}