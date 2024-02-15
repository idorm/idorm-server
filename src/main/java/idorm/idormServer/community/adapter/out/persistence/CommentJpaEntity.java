package idorm.idormServer.community.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import idorm.idormServer.member.adapter.out.persistence.MemberJpaEntity;
import idorm.idormServer.report.adapter.out.persistence.ReportJpaEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentJpaEntity {

	private static final int BLOCKED_CONDITION = 5;
	private static final String BLIND_COMMENT_MESSAGE = "블라인드 처리된 댓글입니다.";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long id;

	private String nickname;

	@Embedded
	private ContentEmbeddedEntity content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_comment_id")
	private CommentJpaEntity parent;

	@OneToMany(mappedBy = "parent")
	private List<CommentJpaEntity> children = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private PostJpaEntity post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private MemberJpaEntity member;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;

	private Boolean isDeleted;

	@OneToMany(mappedBy = "comment")
	private List<ReportJpaEntity> reports = new ArrayList<>();
}