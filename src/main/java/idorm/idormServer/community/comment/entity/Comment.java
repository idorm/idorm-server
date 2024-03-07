package idorm.idormServer.community.comment.entity;

import static idorm.idormServer.community.comment.adapter.out.CommentResponseCode.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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
import idorm.idormServer.common.util.Validator;
import idorm.idormServer.community.comment.adapter.out.exception.AccessDeniedCommentException;
import idorm.idormServer.community.post.entity.Post;
import idorm.idormServer.member.entity.Member;
import idorm.idormServer.report.entity.CommentReport;
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

	@OneToMany(mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
	private List<Comment> child = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	private Boolean isAnonymous;
	private Boolean isDeleted;
	private Boolean isBlocked;

	@OneToMany(mappedBy = "reportedComment")
	private List<CommentReport> reports = new ArrayList<>();

	public Comment(final String content, final Comment parent, final Post post, final Member member,
		final Boolean isAnonymous) {
		validateConsturctor(content, isAnonymous);
		this.content = content;
		this.parent = parent;
		this.post = post;
		this.member = member;
		this.isAnonymous = isAnonymous;
		this.isDeleted = false;
		this.isBlocked = false;

		post.addComment(this);
	}

	public static Comment parent(final String content, final Boolean isAnonymous, final Post post,
		final Member member) {
		return new Comment(content, null, post, member, isAnonymous);
	}

	public static Comment child(final Boolean isAnonymous, final String content, final Comment parent,
		final Post postDomain, final Member member) {
		return new Comment(content, parent, postDomain, member, isAnonymous);
	}

	public String getContentValue() {
		if (isBlocked()) {
			return BLIND_COMMENT_MESSAGE;
		}
		return this.content;
	}

	public void addReport(final CommentReport report) {
		this.reports.add(report);
	}

	private boolean isBlocked() {
		return reports.size() >= BLOCKED_CONDITION;
	}

	public void delete(final Member member) {
		validateOwner(member);
		this.isDeleted = true;
	}

	public boolean isNotOwner(final Member member) {
		return !this.member.equals(member);
	}

	private void validateConsturctor(String content, Boolean isAnonymous) {
		validateContent(content);
		Validator.validateNotNull(isAnonymous);
	}

	private void validateContent(final String content) {
		Validator.validateNotBlank(content);
		Validator.validateLength(content, MIN_LENGTH, MAX_LENGTH, INVALID_CONTENT_LENGTH);
	}

	private void validateOwner(Member member) {
		if (this.getMember() == null || !this.member.equals(member)) {
			throw new AccessDeniedCommentException();
		}
	}
}