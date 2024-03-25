package idorm.idormServer.community.postLike.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import idorm.idormServer.community.postLike.entity.PostLike;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

	List<PostLike> findByMemberId(Long memberId);

	boolean existsByMemberIdAndPostId(Long memberId, Long postId);

	Optional<PostLike> findByMemberIdAndPostId(Long memberId, Long postId);
}
