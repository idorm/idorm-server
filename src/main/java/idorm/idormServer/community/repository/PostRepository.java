package idorm.idormServer.community.repository;

import idorm.idormServer.community.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Post findByIdAndIsDeletedIsFalse(Long id);

    Page<Post> findAllByDormCategoryAndIsDeletedIsFalseOrderByCreatedAtDesc(Character dormCategory, Pageable pageable);

    @Query(value = "SELECT *" +
            "FROM post p " +
            "WHERE p.dorm_category = :dormCategory AND " +
            "p.created_at BETWEEN DATE_ADD(NOW(), INTERVAL -1 WEEK) AND NOW() AND " +
            "p.is_deleted = 0 " +
            "ORDER BY p.post_liked_cnt DESC, p.created_at DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Post> findTopPostsByDormCategory(@Param("dormCategory") Character dormCategory);

    @Query(value = "SELECT *" +
            "FROM post p " +
            "WHERE p.dorm_category = :dormCategory " +
            "AND p.created_at BETWEEN DATE_ADD(DATE_SUB(NOW(), INTERVAL 1 WEEK), INTERVAL 9 HOUR) AND DATE_ADD(NOW(), INTERVAL 9 HOUR) " +
            "AND p.is_deleted = 0 " +
            "ORDER BY p.post_liked_cnt DESC, p.created_at DESC " +
            "LIMIT 1", nativeQuery = true)
    Post findTopPostByDormCategory(@Param("dormCategory") Character dormCategory);

    List<Post> findAllByMemberIdAndIsDeletedIsFalseOrderByUpdatedAtDesc(Long memberId);
}
