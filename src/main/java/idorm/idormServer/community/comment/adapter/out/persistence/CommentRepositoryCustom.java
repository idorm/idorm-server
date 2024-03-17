package idorm.idormServer.community.comment.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import idorm.idormServer.community.comment.entity.Comment;

@Repository
public interface CommentRepositoryCustom {
	Comment findOneByCommentIdAndPostId(Long commentId, Long postId);

	List<Comment> findAllByPostId(Long postId);

	List<Comment> findAllByMemberId(Long memberId);
}
