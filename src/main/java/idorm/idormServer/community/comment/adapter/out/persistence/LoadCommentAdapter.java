package idorm.idormServer.community.comment.adapter.out.persistence;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.community.comment.adapter.out.exception.NotFoundCommentException;
import idorm.idormServer.community.comment.application.port.out.LoadCommentPort;
import idorm.idormServer.community.comment.entity.Comment;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadCommentAdapter implements LoadCommentPort {

	private final CommentRepository commentRepository;

	@Override
	public Comment findById(Long commentId) {
		Comment response = commentRepository.findByIdAndIsDeletedIsFalse(commentId)
			.orElseThrow(NotFoundCommentException::new);
		return response;
	}

	@Override
	public Comment findOneByCommentIdAndPostId(Long commentId, Long postId) {
		return commentRepository.findOneByCommentIdAndPostId(commentId, postId);
	}

	@Override
	public List<Comment> findAllByPostId(Long postId) {
		List<Comment> responses = commentRepository.findAllByPostId(postId);
		return responses.isEmpty() ? new ArrayList<>() : responses;
	}

	@Override
	public List<Comment> findAllByMemberId(Long memberId) {
		List<Comment> responses = commentRepository.findAllByMemberId(memberId);
		return responses.isEmpty() ? new ArrayList<>() : responses;
	}
}