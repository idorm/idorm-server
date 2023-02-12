package idorm.idormServer.community.repository;

import idorm.idormServer.community.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 멤버가 작성한 모든 댓글 반환
     */
    List<Comment> findAllByMemberIdAndIsDeletedFalseOrderByCreatedAtDesc(Long memberId);

    /**
     * 게시글 식별자를 통해서 게시글 내에서 조회되는 모든 댓글 반환
     */
    List<Comment> findAllByPostIdOrderByCreatedAtDesc(Long postId);

    /**
     * 부모 댓글 식별자를 통해서 조회되는 모든 대댓글 반환
     */
    List<Comment> findAllByPostIdAndParentCommentIdOrderByCreatedAt(Long postId, Long parentCommentId);

    /**
     * 부모 댓글 식별자와 게시글 식별자로 해당하는 댓글이 있는지 확인
     */
    boolean existsByIdAndPostId(Long commentId, Long postId);

    /**
     * 멤버가 작성한 삭제된 댓글을 포함한 모든 댓글 반환
     */
    List<Comment> findAllByMemberId(Long memberId);
}
