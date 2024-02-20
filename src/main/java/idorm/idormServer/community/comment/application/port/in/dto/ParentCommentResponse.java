package idorm.idormServer.community.comment.application.port.in.dto;

import java.time.LocalDateTime;
import java.util.List;

import idorm.idormServer.community.comment.domain.Comment;

public record ParentCommentResponse(
	Long commentId,
	Long memberId,
	Long postId,
	String nickname,
	String profileUrl,
	String content,
	Boolean isDeleted,
	Boolean isAnonymous,
	List<CommentResponse> subComments,
	LocalDateTime createdAt
) {
	public static List<ParentCommentResponse> of(final List<Comment> comments) {
		List<ParentCommentResponse> responses = comments.stream()
			.map(ParentCommentResponse::of)
			.toList();
		return responses;
	}

	private static ParentCommentResponse of(final Comment comment) {
		return new ParentCommentResponse(
			comment.getId(),
			comment.getMember().getId(),
			comment.getPost().getId(),
			isAnonymous(comment),
			isProfileUrl(comment),
			comment.getContent().getValue(),
			comment.getIsDeleted(),
			comment.getIsAnonymous(),
			CommentResponse.from(comment.getChild()),
			comment.getCreatedAt()
		);
	}

	private static String isAnonymous(Comment comment) {
		if (comment.getMember().getMemberStatus().equals("DELETED")) {
			return null;
		} else if (comment.getIsAnonymous()) {
			return "익명";
		} else {
			return comment.getMember().getNickname().getValue();
		}
	}

	private static String isProfileUrl(Comment comment) {
		return (comment.getMember().getMemberPhoto() != null) ? // TODO: Member 메서드 생성
			comment.getMember().getMemberPhoto().getValue() : null;
	}
}
