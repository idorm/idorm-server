package idorm.idormServer.community.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.community.domain.Post;
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
			post.getWriterNickname(),
			post.getIsDeleted(),
			post.getCreatedAt(),
			post.getUpdatedAt(),
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
			entity.getWriterNickname(),
			entity.getIsDeleted(),
			memberMapper.toDomain(entity.getMember()),
			postPhotoMapper.toDomain(entity.getPostPhotoJpaEntities()),
			postLikeMapper.toDomain(entity.getPostLikeEntities()),
			commentMapper.toDomain(entity.getCommentJpaEntities()),
			reportMapper.toDomain(entity.getReports()),
			entity.getCreatedAt(),
			entity.getUpdatedAt()
		);
	}

	public List<Post> toDomain(List<PostJpaEntity> entities) {
		List<Post> result = entities.stream()
			.map(this::toDomain)
			.toList();
		return result;
	}
}