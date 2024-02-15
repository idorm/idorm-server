package idorm.idormServer.community.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.exception.ExceptionCode;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.report.domain.Report;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment {

	private static final int BLOCKED_CONDITION = 5;
	private static final String BLIND_COMMENT_MESSAGE = "블라인드 처리된 댓글입니다.";

	private Long id;
	private String nickname;
	private Content content;
	private Comment parent;
	private List<Comment> children = new ArrayList<>();
	private Post post;
	private Member member;
	private Boolean isDeleted;
	private List<Report> reports = new ArrayList<>();
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private Comment(String nickname, CommentContent content, Comment parent, Post post, Member member) {
		this.nickname = nickname;
		this.content = content;
		this.parent = parent;
		this.post = post;
		this.member = member;
		this.isDeleted = false;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();

		post.addComment(this);
	}

	public static Comment parent(String nickname, CommentContent content, Post post, Member member) {
		return new Comment(nickname, content, null, post, member);
	}

	public static Comment child(String nickname, CommentContent content, Comment parent, Post post, Member member) {
		Comment child = new Comment(nickname, content, parent, post, member);
		parent.getChildren().add(child);
		return child;
	}

	public static Comment forMapper(final Long id,
		final String nickname,
		final Content content,
		final Comment parent,
		final List<Comment> childs,
		final Post post,
		final Member member,
		final Boolean isDeleted,
		final List<Report> reports,
		final LocalDateTime createdAt,
		final LocalDateTime updatedAt) {

		return new Comment(id, nickname, content, parent, childs, post, member, isDeleted, reports, createdAt,
			updatedAt);
	}

	public void validateOwner(Member member) {
		if (!member.equals(this.member)) {
			throw new CustomException(null, ExceptionCode.ACCESS_DENIED_COMMENT);
		}
	}

	public boolean isPostWriter() {
		return post.getMember().equals(member);
	}

	public void addReport(Report commentReport) {
		reports.add(commentReport);
	}

	public void deleteChild(Comment child) {
		children.remove(child);
	}

	public boolean isParent() {
		return Objects.isNull(parent);
	}

	public Content getContent() {
		return content;
	}

	public String getContentValue() {
		if (isBlocked()) {
			return BLIND_COMMENT_MESSAGE;
		}
		return content.getValue();
	}

	private boolean isBlocked() {
		return reports.size() >= BLOCKED_CONDITION;
	}

	public void delete() {
		this.isDeleted = true;
		post.deleteComment(this);
	}
}