package idorm.idormServer.report.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.community.comment.entity.Comment;
import idorm.idormServer.member.entity.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("C")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentReport extends Report {

	@Enumerated(value = EnumType.STRING)
	private CommunityReason reasonType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reported_comment_id")
	private Comment reportedComment;

	public CommentReport(Member reporterMember, Member reportedMember, Comment reportedComment,
		CommunityReason reasonType, String reason) {
		super(reporterMember, reportedMember, reason);
		Validator.validateNotNull(reportedComment);
		this.reportedComment = reportedComment;
		this.reasonType = reasonType;

		reportedComment.addReport(this);
	}
}
