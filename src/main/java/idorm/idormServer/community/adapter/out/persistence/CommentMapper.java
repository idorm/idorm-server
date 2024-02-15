package idorm.idormServer.community.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.community.domain.Comment;
import idorm.idormServer.member.adapter.out.persistence.MemberMapper;
import idorm.idormServer.report.adapter.out.persistence.ReportMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentMapper {

	private ContentMapper contentMapper;
	private PostMapper postMapper;
	private MemberMapper memberMapper;
	private ReportMapper reportMapper;

	public CommentJpaEntity toEntity(Comment comment) {
		return new CommentJpaEntity(comment.getId(),
			comment.getNickname(),
			contentMapper.toEntity(comment.getContent()),
			toEntity(comment.getParent()),
			toEntity(comment.getChildren()),
			postMapper.toEntity(comment.getPost()),
			memberMapper.toEntity(comment.getMember()),
			comment.getCreatedAt(),
			comment.getUpdatedAt(),
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
			entity.getNickname(),
			contentMapper.toDomain(entity.getContent()),
			toDomain(entity.getParent()),
			toDomain(entity.getChildren()),
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