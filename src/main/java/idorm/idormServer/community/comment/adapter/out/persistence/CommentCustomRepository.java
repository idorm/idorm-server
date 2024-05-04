package idorm.idormServer.community.comment.adapter.out.persistence;

import idorm.idormServer.community.comment.entity.Comment;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface CommentCustomRepository {

	Comment findOneByCommentIdAndPostId(Long commentId, Long postId);

	List<Comment> findAllByPostId(Long postId);

	List<Comment> findAllByMemberId(Long memberId);
}