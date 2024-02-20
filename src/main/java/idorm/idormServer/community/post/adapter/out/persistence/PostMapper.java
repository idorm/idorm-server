package idorm.idormServer.community.post.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.community.comment.adapter.out.persistence.CommentMapper;
import idorm.idormServer.community.postLike.adapter.out.persistence.PostLikeMapper;
import idorm.idormServer.community.post.domain.Post;
import idorm.idormServer.community.postPhoto.adapter.out.persistence.PostPhotoMapper;
import idorm.idormServer.member.adapter.out.persistence.MemberMapper;
import idorm.idormServer.report.adapter.out.persistence.ReportMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostMapper {

	private final TitleMapper titleMapper;
	private final ContentMapper contentMapper;
	private final MemberMapper memberMapper;
	private final PostPhotoMapper postPhotoMapper;
	private final PostLikeMapper postLikeMapper;
	private final CommentMapper commentMapper;
	private final ReportMapper reportMapper;

	public PostJpaEntity toEntity(Post post) {
		return new PostJpaEntity(post.getId(),
			post.getDormCategory(),
			titleMapper.toEntity(post.getTitle()),
			contentMapper.toEntity(post.getContent()),
			post.getLikeCount(),
			post.getIsDeleted(),
			post.getIsBlocked(),
			post.getIsAnonymous(),
			memberMapper.toEntity(post.getMember()),
			postPhotoMapper.toEntity(post.getPostPhotos()),
			postLikeMapper.toEntity(post.getPostLikes()),
			commentMapper.toEntity(post.getComments()),
			reportMapper.toEntity(post.getReports()));
	}

	public List<PostJpaEntity> toEntity(List<Post> posts) {
		List<PostJpaEntity> result = posts.stream()
			.map(this::toEntity)
			.toList();
		return result;
	}

	public Post toDomain(PostJpaEntity entity) {
		return Post.forMapper(entity.getId(),
			entity.getDormCategory(),
			titleMapper.toDomain(entity.getTitle()),
			contentMapper.toDomain(entity.getContent()),
			entity.getLikeCount(),
			entity.getIsBlocked(),
			entity.getIsDeleted(),
			entity.getIsAnonymous(),
			memberMapper.toDomain(entity.getMember()),
			postPhotoMapper.toDomain(entity.getPostPhotos()),
			postLikeMapper.toDomain(entity.getPostLikes()),
			commentMapper.toDomain(entity.getComments()),
			reportMapper.toDomain(entity.getReports()),
			entity.getCreatedAt(),
			entity.getUpdatedAt());
	}

	public List<Post> toDomain(List<PostJpaEntity> entities) {
		List<Post> result = entities.stream()
			.map(this::toDomain)
			.toList();
		return result;
	}
}