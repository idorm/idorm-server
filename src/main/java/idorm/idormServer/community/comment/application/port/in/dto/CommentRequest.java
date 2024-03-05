package idorm.idormServer.community.comment.application.port.in.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public record CommentRequest(
    @NotBlank(message = "댓글은 공백일 수 없습니다.")
    @Size(min = 1, max = 50, message = "content , 댓글 내용은 1~50자 이내여야 합니다.")
    String content,
    @NotNull(message = "익명 여부는 공백일 수 없습니다.")
    Boolean isAnonymous,
    @Positive(message = "parentId , 부모 댓글 식별자는 양수만 가능합니다.")
    Long parentCommentId
) {

  public boolean isParent() {
    return parentCommentId == null;
  }
}