package idorm.idormServer.community.comment.application.port.in.dto;

import idorm.idormServer.community.comment.entity.Comment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record CommentResponse(Long commentId,
                              Long memberId,
                              Long parentId,
                              Long postId,
                              Boolean isDeleted,
                              Boolean isAnonymous,
                              String nickname,
                              String profileUrl,
                              String content,
                              LocalDateTime createdAt) {

  public static List<CommentResponse> from(final List<Comment> comments) {
    List<CommentResponse> responses = comments.stream()
        .map(CommentResponse::from)
        .toList();
    return responses;
  }

  private static CommentResponse from(final Comment comment) {
    Map<Long, Integer> memberAnonymousCountMap = ParentCommentResponse.memberAnonymousCountMap;
    return new CommentResponse(
        comment.getId(),
        comment.getMember().getId(),
        (comment.getParent() != null) ? comment.getParent().getId() : null,
        comment.getPost().getId(),
        comment.getIsDeleted(),
        comment.getIsAnonymous(),
        isAnonymous(comment, memberAnonymousCountMap),
        comment.getMember().getProfilePhotoUrl(),
        comment.getContentValue(),
        comment.getCreatedAt()
    );
  }

  private static String isAnonymous(Comment comment, Map<Long, Integer> memberAnonymousCountMap) {
    if (comment.getMember().getMemberStatus().equals("DELETED")) {
      return "-999";
    } else if (comment.getIsAnonymous()) {
      return "익명" + ParentCommentResponse.memberAnonymousCountMap.get(comment.getMember().getId());
    } else {
      return comment.getMember().getNickname().getValue();
    }
  }
}