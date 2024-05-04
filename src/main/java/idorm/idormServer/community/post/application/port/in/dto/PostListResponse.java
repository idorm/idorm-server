package idorm.idormServer.community.post.application.port.in.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record PostListResponse(
	Long postId,
	Long memberId,
	String dormCategory,
	String title,
	String content,
	String nickname,
	Boolean isAnonymous,
	int likesCount,
	Long commentsCount,
	int imagesCount,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {

	public static List<PostListResponse> of(List<PostListData> posts) {
		return posts.stream()
			.map(PostListResponse::new)
			.collect(Collectors.toList());
	}

	public PostListResponse(PostListData post) {
		this(
			post.postId(),
			post.memberId(),
			post.dormCategory(),
			post.title(),
			post.content(),
			post.nickname(),
			post.isAnonymous(),
			post.likesCount(),
			post.commentsCount(),
			post.imagesCount(),
			post.createdAt(),
			post.updatedAt()
		);
	}
}