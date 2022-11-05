package idorm.idormServer.community.repository;

import idorm.idormServer.community.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 멤버가 작성한 모든 댓글 반환
     */
    @Query(value = "SELECT * " +
            "FROM comment c " +
            "WHERE c.member_id = :memberId AND " +
            "c.is_visible = 1", nativeQuery = true)
    List<Comment> findCommentsByMemberId(@Param("memberId") Long memberId);

    @Query(value = "SELECT * " +
            "FROM comment c " +
            "WHERE c.post_id = :postId AND " +
            "c.comment_id = :commentId AND ", nativeQuery = true)
    Optional<Comment> findCommentByPostIdAndCommentId(@Param("postId") Long postId,
                                                      @Param("commentId") Long commentId);

    /**
     * 게시글 식별자를 통해서 게시글 내에서 조회되는 모든 댓글 반환
     */
    @Query(value = "SELECT * " +
            "FROM comment c " +
            "WHERE c.post_id = :postId", nativeQuery = true)
    List<Comment> findCommentsByPostId(@Param("postId") Long postId);

    /**
     * 부모 댓글 식별자를 통해서 조회되는 모든 대댓글 반환
     */
    @Query(value = "SELECT * " +
            "FROM comment c " +
            "WHERE c.parent_comment_id = :parentCommentId AND " +
            "c.post_id = :postId", nativeQuery = true)
    List<Comment> findSubCommentsByParentCommentId(@Param("parentCommentId") Long parentCommentId,
                                                   @Param("postId") Long postId);
}
