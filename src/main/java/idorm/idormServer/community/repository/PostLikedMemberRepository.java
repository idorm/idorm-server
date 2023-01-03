package idorm.idormServer.community.repository;

import idorm.idormServer.community.domain.PostLikedMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostLikedMemberRepository extends JpaRepository<PostLikedMember, Long> {

    @Query(value = "SELECT post_id " +
            "FROM post_liked_member plm " +
            "WHERE plm.member_id = :memberId " +
            "ORDER BY plm.created_at DESC", nativeQuery = true)
    List<Long> findLikedPostsByMemberId(@Param("memberId") Long memberId);

    void deleteByMemberIdAndPostId(Long memberId, Long postId);

    void deleteAllByPostId(Long postId);

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);
}
