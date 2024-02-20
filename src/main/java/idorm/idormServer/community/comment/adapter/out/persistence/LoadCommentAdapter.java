package idorm.idormServer.community.comment.adapter.out.persistence;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.community.comment.application.port.out.LoadCommentPort;
import idorm.idormServer.community.comment.domain.Comment;
import idorm.idormServer.community.exception.NotFoundCommentException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadCommentAdapter implements LoadCommentPort {

	private final CommentMapper commentMapper;
	private final CommentRepository commentRepository;

	@Override
	public Comment findById(Long commentId) {
		CommentJpaEntity response = commentRepository.findByIdAndIsDeletedIsFalse(commentId)
			.orElseThrow(NotFoundCommentException::new);
		return commentMapper.toDomain(response);
	}

	@Override
	public Comment findOneByCommentIdAndPostId(Long commentId, Long postId) {
		CommentJpaEntity response = commentRepository.findByIdAndPostId(commentId, postId)
			.orElseThrow(NotFoundCommentException::new);
		return commentMapper.toDomain(response);
	}

	@Override
	public List<Comment> findAllByPostId(Long postId) {
		List<CommentJpaEntity> responses = commentRepository.findAllByPostIdOrderByCreatedAtAsc(
			postId);
		return responses.isEmpty() ? new ArrayList<>() : commentMapper.toDomain(responses);
	}

	@Override
	public List<Comment> findAllByMemberId(Long memberId) {
		List<CommentJpaEntity> responses = commentRepository.findAllByMemberIdAndIsDeletedIsFalseOrderByCreatedAtDesc(
			memberId);
		return responses.isEmpty() ? new ArrayList<>() : commentMapper.toDomain(responses);
	}
}
