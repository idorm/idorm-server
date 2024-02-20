package idorm.idormServer.community.postLike.adapter.out.persistence;

import idorm.idormServer.community.post.adapter.out.persistence.PostJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikeMemberRepository extends JpaRepository<PostLikeJpaEntity, Long> {

  @Query(value = "SELECT post " +
      "FROM post_like_member plm " +
      "WHERE plm.member_id = :memberId AND " +
      "plm.is_deleted = false " +
      "ORDER BY plm.created_at DESC", nativeQuery = true)
  List<PostLikeJpaEntity> findAllByMemberId(@Param("memberId") Long memberId);

  List<PostLikeJpaEntity> findAllByPostAndIsDeletedIsFalse(PostJpaEntity postJpaEntity);

  boolean existsByMemberIdAndPostIdAndIsDeletedIsFalse(Long memberId, Long postId);

  Optional<PostLikeJpaEntity> findByMemberIdAndPostId(Long memberId, Long postId);
}
