package idorm.idormServer.community.comment.application.port.out;

import java.util.List;
import java.util.Optional;

import idorm.idormServer.community.comment.domain.Comment;

public interface LoadCommentPort {

	Comment findById(Long commentId);


	Comment findOneByCommentIdAndPostId(Long commentId, Long postId);


	List<Comment> findAllByPostId(Long postId);

	List<Comment> findAllByMemberId(Long memberId);
}
