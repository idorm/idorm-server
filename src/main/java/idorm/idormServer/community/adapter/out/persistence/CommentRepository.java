package idorm.idormServer.community.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentJpaEntity, Long> {

	CommentJpaEntity findByIdAndIsDeletedIsFalse(Long id);

	/**
	 * 멤버가 작성한 모든 댓글 반환
	 */
	List<CommentJpaEntity> findAllByMemberIdAndIsDeletedIsFalseOrderByCreatedAtDesc(Long memberId);

	/**
	 * 게시글 식별자를 통해서 게시글 내에서 조회되는 모든 댓글 반환
	 */
	List<CommentJpaEntity> findAllByPostIdOrderByCreatedAtAsc(Long postId);

	Optional<CommentJpaEntity> findByIdAndPostId(Long id, Long postId);

	/**
	 * 부모 댓글 식별자를 통해서 조회되는 모든 대댓글 반환
	 */
	List<CommentJpaEntity> findAllByPostIdAndParentCommentIdOrderByCreatedAt(Long postId, Long parentCommentId);

	/**
	 * 부모 댓글 식별자와 게시글 식별자로 해당하는 댓글이 있는지 확인
	 */
	boolean existsByIdAndPostId(Long commentId, Long postId);
}
