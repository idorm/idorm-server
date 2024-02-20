package idorm.idormServer.community.comment.application.port.in.dto;

import java.time.LocalDateTime;
import java.util.List;

import idorm.idormServer.community.comment.domain.Comment;

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
		return new CommentResponse(
			comment.getId(),
			comment.getMember().getId(),
			comment.getParent().getId(),
			comment.getPost().getId(),
			comment.getIsDeleted(),
			comment.getIsAnonymous(),
			isAnonymous(comment),
			comment.getMember().getMemberPhoto().getValue(),
			comment.getContentValue(),
			comment.getCreatedAt()
		);
	}

	private static String isAnonymous(Comment comment) {
		if (comment.getMember().getMemberStatus().equals("DELETED")) { // TODO: Member 메서드 작성
			return "-999";
		} else if (comment.getIsAnonymous()) {
			return "익명";
		} else {
			return comment.getMember().getNickname().getValue();
		}
	}
}