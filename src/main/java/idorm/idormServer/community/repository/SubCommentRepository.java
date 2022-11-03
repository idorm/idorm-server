package idorm.idormServer.community.repository;

import idorm.idormServer.community.domain.SubComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubCommentRepository extends JpaRepository<SubComment, Long> {

    /**
     * 멤버 아이디를 통해서 멤버가 작성한 대댓글 반환
     */
    @Query(value = "SELECT * " +
            "FROM sub_comment sc " +
            "WHERE sc.member_id = :memberId AND " +
            "sc.is_visible = 1 " +
            "ORDER BY sc.updated_at", nativeQuery = true)
    List<SubComment> findSubCommentsByMemberId(@Param("memberId") Long memberId);
}
