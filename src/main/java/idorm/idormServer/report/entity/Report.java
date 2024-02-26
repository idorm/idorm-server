package idorm.idormServer.report.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.community.comment.entity.Comment;
import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // pg. 249
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reporter_member_id")
	private Member reporterMember;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reported_member_id")
	private Member reportedMember;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reported_post_id")
	private Post reportedPost;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reported_comment_id")
	private Comment reportedComment;

	private Character reasonType;

	private String reason;

	@Column(updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
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
}