package idorm.idormServer.community.post.application.port.in.dto;

import java.time.LocalDateTime;
import java.util.List;

import idorm.idormServer.community.comment.application.port.in.dto.ParentCommentResponse;
import idorm.idormServer.community.comment.domain.Comment;
import idorm.idormServer.community.post.domain.Post;
import idorm.idormServer.community.postPhoto.application.port.in.dto.PostPhotoResponse;
import idorm.idormServer.community.postPhoto.domain.PostPhoto;

public record PostResponse(

	Long postId,
	Long memberId,
	String dormCategory,
	String title,
	String content,
	String nickname,
	String profileUrl,
	int likesCount,
	Boolean isLiked,
	LocalDateTime createdAt,
	LocalDateTime updatedAt,
	Boolean isAnonymous,
	List<PostPhotoResponse> postPhotos,
	List<ParentCommentResponse> comments
) {

	public static PostResponse of(Post post, List<PostPhoto> postPhotos, List<Comment> comments, Boolean isLiked) {

		return new PostResponse(post.getId(),
			post.getMember().getId(),
			post.getDormCategory().toString(),
			post.getTitle().getValue(),
			post.getContent().getValue(),
			post.getMember().getNickname().getValue(),
			post.getMember().getMemberPhoto().getValue(),
			post.getLikeCount(),
			isLiked,
			post.getCreatedAt(),
			post.getUpdatedAt(),
			post.getIsAnonymous(),
			PostPhotoResponse.of(postPhotos),
			ParentCommentResponse.of(comments));
	}
}