package idorm.idormServer.community.post.adapter.out.persistence;

import idorm.idormServer.matchingInfo.domain.DormCategory;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<PostJpaEntity, Long> {

  PostJpaEntity findByIdAndIsDeletedIsFalse(Long id);

  Page<PostJpaEntity> findAllByDormCategoryAndIsDeletedIsFalseOrderByCreatedAtDesc(
      DormCategory dormCategory,
      Pageable pageable);

  @Query(value = "SELECT *"
      + "FROM postJpaEntity p "
      + "WHERE p.member.id = :memberId "
      + "AND p.likes.member_id = :memberId "
      + "AND p.is_deleted = false", nativeQuery = true)
  List<PostJpaEntity> findLikePostsByMemberId(Long memberId);

  @Query(value = "SELECT *" +
      "FROM postJpaEntity p " +
      "WHERE p.dorm_category = :dormCategory " +
      "AND p.created_at BETWEEN DATE_ADD(DATE_SUB(NOW(), INTERVAL 1 WEEK), INTERVAL 9 HOUR) AND DATE_ADD(NOW(), INTERVAL 9 HOUR) "
      +
      "AND p.is_deleted = 0 " +
      "ORDER BY p.post_liked_cnt DESC, p.created_at DESC " +
      "LIMIT 10", nativeQuery = true)
  List<PostJpaEntity> findTopPostsByDormCategory(@Param("dormCategory") DormCategory dormCategory);

  @Query(value = "SELECT *" +
      "FROM postJpaEntity p " +
      "WHERE p.dorm_category = :dormCategory " +
      "AND p.created_at BETWEEN DATE_ADD(DATE_SUB(NOW(), INTERVAL 1 WEEK), INTERVAL 9 HOUR) AND DATE_ADD(NOW(), INTERVAL 9 HOUR) "
      +
      "AND p.is_deleted = 0 " +
      "ORDER BY p.post_liked_cnt DESC, p.created_at DESC " +
      "LIMIT 1", nativeQuery = true)
  PostJpaEntity findTopPostByDormCategory(@Param("dormCategory") Character dormCategory);

  List<PostJpaEntity> findAllByMemberIdAndIsDeletedIsFalseOrderByUpdatedAtDesc(Long memberId);
}
