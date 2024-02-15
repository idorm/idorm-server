package idorm.idormServer.report.domain;

import java.time.LocalDateTime;
import java.util.List;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.community.domain.Post;
import idorm.idormServer.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Report {

	private Long id;
	private Member reportedMember;
	private Member reporterMember;
	private Post reportedPost;
	private idorm.idormServer.community.domain.Comment reportedComment;
	private Character reasonType;
	private String reason;
	private LocalDateTime createdAt;

	private Report(Member reporterMember,
		Member reportedMember,
		Post reportedPost,
		Comment reportedComment,
		Character reasonType,
		String reason) {
		this.reporterMember = reporterMember;
		this.reportedMember = reportedMember;
		this.reportedPost = reportedPost;
		this.reportedComment = reportedComment;
		this.reasonType = reasonType;
		this.reason = reason;
		this.createdAt = LocalDateTime.now();
	}

	public static Report memberReport(final Member reporterMember,
		final Member reportedMember,
		final MemberReason reasonType,
		final String reason) {
		Validator.validateNotNull(List.of(reportedMember, reporterMember, reasonType));
		return new Report(reporterMember,
			reportedMember,
			null,
			null,
			reasonType.getType(),
			reason);
	}

	public static Report postReport(final Member reporterMember,
		final Member reportedMember,
		final Post reportedPost,
		final CommunityReason reasonType,
		final String reason) {

		Validator.validateNotNull(List.of(reportedMember, reporterMember, reportedPost, reasonType));
		return new Report(reporterMember,
			reportedMember,
			reportedPost,
			null,
			reasonType.getType(),
			reason);
	}

	public static Report commentReport(final Member reporterMember,
		final Member reportedMember,
		final Comment reportedComment,
		final CommunityReason reasonType,
		final String reason) {

		Validator.validateNotNull(List.of(reportedMember, reporterMember, reportedComment, reasonType));
		return new Report(reporterMember,
			reportedMember,
			null,
			reportedComment,
			reasonType.getType(),
			reason);
	}

	public static Report forMapper(final Long id,
		final Member reportedMember,
		final Member reporterMember,
		final Post reportedPost,
		final Comment reportedComment,
		final Character reasonType,
		final String reason,
		final LocalDateTime createdAt) {
		return new Report(id, reportedMember, reporterMember, reportedPost, reportedComment, reasonType, reason,
			createdAt);
	}
}