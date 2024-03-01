package idorm.idormServer.community.comment.application.port.in.dto;

import idorm.idormServer.community.comment.entity.Comment;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record ParentCommentResponse(
    Long commentId,
    Long memberId,
    Long postId,
    String nickname,
    String profileUrl,
    String content,
    Boolean isDeleted,
    Boolean isAnonymous,
    LocalDateTime createdAt,
    List<CommentResponse> subComments
) {

  public static List<ParentCommentResponse> of(final List<Comment> comments) {
    sortedAnonymous(comments);
    List<ParentCommentResponse> responses = comments.stream()
        .filter(comment -> comment.getParent() == null)
        .map(ParentCommentResponse::of)
        .toList();
    return responses;
  }

  private static ParentCommentResponse of(final Comment comment) {
    List<CommentResponse> childResponses = comment.getChild() != null
        ? CommentResponse.from(comment.getChild())
        : null;
    return new ParentCommentResponse(
        comment.getId(),
        comment.getMember().getId(),
        comment.getPost().getId(),
        isAnonymous(comment),
        isProfileUrl(comment),
        comment.getContent(),
        comment.getIsDeleted(),
        comment.getIsAnonymous(),
        comment.getCreatedAt(),
        childResponses
    );
  }

  static Map<Long, Integer> memberAnonymousCountMap = new HashMap<>();
  static int totalAnonymousCount = 0;

  private static void sortedAnonymous(List<Comment> comments) {
    comments.stream()
        .sorted(Comparator.comparing(Comment::getCreatedAt))
        .map(comment -> comment.getMember().getId())
        .distinct()
        .forEach(comment -> memberAnonymousCountMap.computeIfAbsent(comment, k -> ++totalAnonymousCount));
  }

  private static String isAnonymous(Comment comment) {
    if (comment.getMember().getMemberStatus().equals("DELETED")) {
      return "-999";
    } else if (comment.getIsAnonymous()) {
      return "익명" + memberAnonymousCountMap.get(comment.getMember().getId());
    } else {
      return comment.getMember().getNickname().getValue();
    }
  }

  private static String isProfileUrl(Comment comment) {
    return (comment.getMember().getProfilePhotoUrl() != null) ?
        comment.getMember().getProfilePhotoUrl() : null;
  }
}
