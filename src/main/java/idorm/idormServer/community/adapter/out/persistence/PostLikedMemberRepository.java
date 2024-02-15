package idorm.idormServer.community.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikedMemberRepository extends JpaRepository<PostLikeJpaEntity, Long> {

	@Query(value = "SELECT post_id " +
		"FROM post_liked_member plm " +
		"WHERE plm.member_id = :memberId AND " +
		"plm.is_deleted = 0 " +
		"ORDER BY plm.created_at DESC", nativeQuery = true)
	List<Long> findAllByMemberId(@Param("memberId") Long memberId);

	List<PostLikeJpaEntity> findAllByPostAndIsDeletedIsFalse(PostJpaEntity postJpaEntity);

	boolean existsByMemberIdAndPostIdAndIsDeletedIsFalse(Long memberId, Long postId);

	Optional<PostLikeJpaEntity> findByMemberIdAndPostIdAndIsDeletedIsFalse(Long memberId, Long postId);
}
