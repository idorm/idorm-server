package idorm.idormServer.community.repository;

import idorm.idormServer.community.domain.PostLikedMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostLikedMemberRepository extends JpaRepository<PostLikedMember, Long> {

    @Query(value = "SELECT post_id " +
            "FROM post_liked_member plm " +
            "WHERE plm.member_id = :memberId " +
            "ORDER BY plm.created_at DESC", nativeQuery = true)
    List<Long> findAllLikedPostByMemberId(@Param("memberId") Long memberId);

    List<PostLikedMember> findAllByMemberId(Long memberId);

    void deleteByMemberIdAndPostId(Long memberId, Long postId);

    void deleteAllByPostId(Long postId);

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    Optional<PostLikedMember> findByMemberIdAndPostId(Long memberId, Long postId);
}
