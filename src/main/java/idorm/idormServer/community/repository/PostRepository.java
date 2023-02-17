package idorm.idormServer.community.repository;

import idorm.idormServer.community.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByDormCategoryAndIsDeletedFalseOrderByCreatedAtDesc(Character dormCategory, Pageable pageable);

    @Query(value = "SELECT *" +
            "FROM post p " +
            "WHERE p.dorm_category = :dormCategory AND " +
            "p.created_at BETWEEN DATE_ADD(NOW(), INTERVAL -1 WEEK) AND NOW() AND " +
            "p.is_deleted = 0 " +
            "ORDER BY p.post_liked_cnt DESC, p.created_at DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Post> findTopPostsByDormCategory(@Param("dormCategory") Character dormCategory);

    List<Post> findAllByMemberIdAndIsDeletedFalseOrderByUpdatedAtDesc(Long memberId);

    // 멤버 탈퇴 시 사용, 삭제된 게시글도 전부 조회
    List<Post> findAllByMemberId(Long memberId);
}
