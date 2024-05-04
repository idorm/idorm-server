package idorm.idormServer.community.post.application.port.in.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import idorm.idormServer.community.post.application.port.in.dto.querydsl.PostSearchResponse;

public record PostOneSummaryResponse(
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

	public static List<PostOneSummaryResponse> of(List<PostSearchResponse> posts) {
		return posts.stream()
			.map(PostOneSummaryResponse::new)
			.collect(Collectors.toList());
	}

	public PostOneSummaryResponse(PostSearchResponse post) {
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