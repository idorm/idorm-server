package idorm.idormServer.community.post.application.port.in.dto;

import java.time.LocalDateTime;
import java.util.List;

import idorm.idormServer.community.comment.application.port.in.dto.ParentCommentResponse;
import idorm.idormServer.community.post.entity.Post;

public record PostListResponse(

	Long postId,
	Long memberId,
	String dormCategory,
	String title,
	String content,
	String nickname,
	Boolean isAnonymous,
	int likesCount,
	int commentsCount,
	int imagesCount,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {

	public static PostListResponse of(final Post post) {
		return new PostListResponse(
				post.getId(),
				post.getMember().getId(),
				post.getDormCategory().toString(),
				post.getTitle(),
				post.getContent(),
			isAnonymous(post),
				post.getIsAnonymous(),
				post.getLikeCount(),
				post.getComments().size(),
				post.getPostPhotos().size(),
				post.getCreatedAt(),
				post.getUpdatedAt()
		);
	}

	public static List<PostListResponse> of(final List<Post> posts) {
		List<PostListResponse> responses = posts.stream()
			.map(PostListResponse::of)
			.toList();
		return responses;
	}

	private static String isAnonymous(Post post) {
		if (post.getMember().getMemberStatus().equals("DELETED")) {
			return "-999";
		} else if (post.getIsAnonymous()) {
			return "익명";
		} else {
			return post.getMember().getNickname().getValue();
		}
	}

	private static int commentCount(List<ParentCommentResponse> comments) {
		int commentCount = comments.size();
		commentCount += comments.stream()
			.mapToInt(comment -> Math.toIntExact(comment.subComments().size())).sum();
		return commentCount;
	}
}