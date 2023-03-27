package idorm.idormServer.community.repository;

import idorm.idormServer.community.domain.Post;
import idorm.idormServer.community.domain.PostLikedMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostLikedMemberRepository extends JpaRepository<PostLikedMember, Long> {

    @Query(value = "SELECT post_id " +
            "FROM post_liked_member plm " +
            "WHERE plm.member_id = :memberId AND " +
            "plm.is_deleted = 0 " +
            "ORDER BY plm.created_at DESC", nativeQuery = true)
    List<Long> findAllByMemberId(@Param("memberId") Long memberId);

    List<PostLikedMember> findAllByPostAndIsDeletedIsFalse(Post post);

    boolean existsByMemberIdAndPostIdAndIsDeletedIsFalse(Long memberId, Long postId);

    Optional<PostLikedMember> findByMemberIdAndPostIdAndIsDeletedIsFalse(Long memberId, Long postId);
}
