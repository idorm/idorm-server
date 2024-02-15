package idorm.idormServer.report.adapter.out.persistence;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import idorm.idormServer.community.adapter.out.persistence.CommentJpaEntity;
import idorm.idormServer.community.adapter.out.persistence.PostJpaEntity;
import idorm.idormServer.member.adapter.out.persistence.MemberJpaEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reported_member_id")
	private MemberJpaEntity reportedMember;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reporter_member_id")
	private MemberJpaEntity reporterMember;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reported_post_id")
	private PostJpaEntity reportedPost;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reported_comment_id")
	private CommentJpaEntity reportedComment;

	private Character reasonType;

	private String reason;

	@Column(updatable = false)
	private LocalDateTime createdAt;
}