package idorm.idormServer.community.comment.domain;

import idorm.idormServer.community.exception.AccessDeniedCommentException;
import idorm.idormServer.community.post.domain.Post;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.report.domain.Report;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment {

  private static final int BLOCKED_CONDITION = 5;
  private static final String BLIND_COMMENT_MESSAGE = "블라인드 처리된 댓글입니다.";

  private Long id;
  private CommentContent content; // TODO: CommentContent 수정
  private Comment parent;
  private List<Comment> child = new ArrayList<>();
  private Post post;
  private Member member;
  private Boolean isDeleted;
  private Boolean isAnonymous;
  private List<Report> reports = new ArrayList<>();
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Comment(final CommentContent content, final Comment parent, final Post post,
      final Member member,
      final Boolean isAnonymous) {
    this.content = content;
    this.parent = parent;
    this.post = post;
    this.member = member;
    this.isAnonymous = isAnonymous;

    post.addComment(this);
  }

  public static Comment parent(final CommentContent content, final Boolean isAnonymous,
      final Post post,
      final Member member) {
    return new Comment(content, null, post, member, isAnonymous);
  }

  public static Comment child(final Boolean isAnonymous,
      final CommentContent content,
      final Comment parent,
      final Post post,
      final Member member) {
    Comment child = new Comment(content, parent, post, member, isAnonymous);
    parent.getChild().add(child);
    return child;
  }

  public static Comment forMapper(final Long id,
      final Boolean isAnonymous,
      final CommentContent content,
      final Comment parent,
      final List<Comment> childs,
      final Post post,
      final Member member,
      final Boolean isDeleted,
      final List<Report> reports,
      final LocalDateTime createdAt,
      final LocalDateTime updatedAt) {

    return new Comment(id, content, parent, childs, post, member, isDeleted, isAnonymous, reports,
        createdAt, updatedAt);
  }

  public boolean isPostWriter() {
    return post.getMember().equals(member);
  }

  public void addReport(Report commentReport) {
    reports.add(commentReport);
  }

  public void deleteChild(Comment child) {
    this.child.remove(child);
  }

  public boolean isParent() {
    return Objects.isNull(parent);
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