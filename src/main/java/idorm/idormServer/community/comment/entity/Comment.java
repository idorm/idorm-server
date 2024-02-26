package idorm.idormServer.community.comment.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import idorm.idormServer.common.entity.BaseTimeEntity;
import idorm.idormServer.community.comment.adapter.out.exception.AccessDeniedCommentException;
import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.member.entity.Member;
import idorm.idormServer.report.entity.Report;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

	private static final int BLOCKED_CONDITION = 5;
	private static final String BLIND_COMMENT_MESSAGE = "블라인드 처리된 댓글입니다.";
	private static final int MIN_LENGTH = 1;
	public static final int MAX_LENGTH = 50;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long id;

	@Column(name = "content", nullable = false, length = MAX_LENGTH)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_comment_id")
	private Comment parent;

	@OneToMany(mappedBy = "parent")
	private List<Comment> child = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	private Boolean isAnonymous;
	private Boolean isDeleted;

	@OneToMany(mappedBy = "reportedComment")
	private List<Report> reports = new ArrayList<>();

	public Comment(final String content, final Comment parent, final Post post, final Member member,
		final Boolean isAnonymous) {
		this.content = content;
		this.parent = parent;
		this.post = post;
		this.member = member;
		this.isAnonymous = isAnonymous;

		post.addComment(this);
	}

	public static Comment parent(final String content, final Boolean isAnonymous, final Post post,
		final Member member) {
		return new Comment(content, null, post, member, isAnonymous);
	}

	public static Comment child(final Boolean isAnonymous, final String content, final Comment parent,
		final Post postDomain, final Member member) {
		Comment child = new Comment(content, parent,
			postDomain, member, isAnonymous);
		parent.getChild().add(child);
		return child;
	}

	public String getContentValue() {
		if (isBlocked()) {
			return BLIND_COMMENT_MESSAGE;
		}
		return this.content;
	}

	private boolean isBlocked() {
		return reports.size() >= BLOCKED_CONDITION;
	}

	public void delete(final Member member) {
		validateOwner(member);
		this.isDeleted = true;
		post.deleteComment(this);
	}

	private void validateOwner(Member member) {
		if (this.getMember() == null || !this.member.equals(member)) {
			throw new AccessDeniedCommentException();
		}
	}
}