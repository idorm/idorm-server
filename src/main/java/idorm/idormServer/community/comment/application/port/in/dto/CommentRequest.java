package idorm.idormServer.community.comment.application.port.in.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CommentRequest(
	@NotBlank(message = "댓글 내용은 공백일 수 없습니다.")
	String content,
	@NotNull(message = "익명 여부는 공백일 수 없습니다.")
	Boolean isAnonymous,
	Long parentCommentId
) {
	public boolean isChild() {
		return parentCommentId != null;
	}
}