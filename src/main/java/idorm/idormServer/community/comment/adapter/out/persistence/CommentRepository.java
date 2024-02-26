package idorm.idormServer.community.comment.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import idorm.idormServer.community.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  Optional<Comment> findByIdAndIsDeletedIsFalse(Long id);

  /**
   * 부모 댓글 식별자를 통해서 조회되는 모든 대댓글 반환
   */
  // List<CommentDomain> findAllByPostIdAndParentCommentIdOrderByCreatedAt(Long postId,
  //     Long parentCommentId);

  /**
   * 부모 댓글 식별자와 게시글 식별자로 해당하는 댓글이 있는지 확인
   */
  // boolean existsByIdAndPostId(Long commentId, Long postId);
}
