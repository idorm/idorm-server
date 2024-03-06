package idorm.idormServer.report.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.member.entity.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("P")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReport extends Report {

	@Enumerated(value = EnumType.STRING)
	private CommunityReason reasonType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reported_post_id")
	private Post reportedPost;

	public PostReport(Member reporterMember, Member reportedMember, Post reportedPost, CommunityReason reasonType,
		String reason) {
		super(reporterMember, reportedMember, reason);
		Validator.validateNotNull(reportedPost);
		this.reportedPost = reportedPost;
		this.reasonType = reasonType;

		reportedPost.addReport(this);
	}
}
