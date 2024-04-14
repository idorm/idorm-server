package idorm.idormServer.community.postLike.adapter.out.persistence;

import idorm.idormServer.community.postLike.entity.PostLike;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long>, PostLikeCustomRepository {

    List<Long> findPostIdsByMemberId(Long memberId);

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    Optional<PostLike> findByMemberIdAndPostId(Long memberId, Long postId);
}
