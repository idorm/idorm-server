package idorm.idormServer.community.comment.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.community.comment.domain.Comment;
import idorm.idormServer.community.post.adapter.out.persistence.PostMapper;
import idorm.idormServer.member.adapter.out.persistence.MemberMapper;
import idorm.idormServer.report.adapter.out.persistence.ReportMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentMapper {

	private final PostMapper postMapper;
	private final MemberMapper memberMapper;
	private final ReportMapper reportMapper;
	private final CommentContentMapper contentMapper;

	public CommentJpaEntity toEntity(Comment comment) {
		return new CommentJpaEntity(comment.getId(),
			contentMapper.toEntity(comment.getContent()),
			toEntity(comment.getParent()),
			toEntity(comment.getChild()),
			postMapper.toEntity(comment.getPost()),
			memberMapper.toEntity(comment.getMember()),
/*        comment.getCreatedAt(),
        comment.getUpdatedAt(),*/
			comment.getIsAnonymous(),
			comment.getIsDeleted(),
			reportMapper.toEntity(comment.getReports()));
	}

	public List<CommentJpaEntity> toEntity(List<Comment> comments) {
		List<CommentJpaEntity> results = comments.stream()
			.map(this::toEntity)
			.toList();
		return results;
	}

	public Comment toDomain(CommentJpaEntity entity) {
		return Comment.forMapper(entity.getId(),
			entity.getIsAnonymous(),
			contentMapper.toDomain(entity.getContent()),
			toDomain(entity.getParent()),
			toDomain(entity.getChild()),
			postMapper.toDomain(entity.getPost()),
			memberMapper.toDomain(entity.getMember()),
			entity.getIsDeleted(),
			reportMapper.toDomain(entity.getReports()),
			entity.getCreatedAt(),
			entity.getUpdatedAt());
	}

	public List<Comment> toDomain(List<CommentJpaEntity> entities) {
		List<Comment> results = entities.stream()
			.map(this::toDomain)
			.toList();
		return results;
	}
}