package idorm.idormServer.community.postLike.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import idorm.idormServer.community.postLike.entity.PostLike;

@Repository
public interface PostLikeMemberRepository extends JpaRepository<PostLike, Long> {

	// @Query(value = "SELECT post " +
	//     "FROM post_like_member plm " +
	//     "WHERE plm.member_id = :memberId AND " +
	//     "plm.is_deleted = false " +
	//     "ORDER BY plm.created_at DESC", nativeQuery = true)
	// List<PostLikeDomain> findAllByMemberId(@Param("memberId") Long memberId);

	List<PostLike> findByMemberId(Long memberId);

	boolean existsByMemberIdAndPostId(Long memberId, Long postId);

	Optional<PostLike> findByMemberIdAndPostId(Long memberId, Long postId);
}
