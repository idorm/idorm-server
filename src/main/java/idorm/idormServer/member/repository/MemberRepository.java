package idorm.idormServer.member.repository;

import idorm.idormServer.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByIdAndIsDeletedIsFalse(Long id);

    Optional<Member> findByEmailAndIsDeletedIsFalse(@Param("email") String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    @Query(value = "SELECT liked_member " +
            "FROM liked_members d " +
            "WHERE d.member_id = :memberId", nativeQuery = true)
    List<Long> findlikedMembersById(@Param("memberId") Long memberId);

    @Query(value = "SELECT disliked_member " +
            "FROM disliked_members d " +
            "WHERE d.member_id = :memberId", nativeQuery = true)
    List<Long> findDislikedMembersById(@Param("memberId") Long memberId);

    @Query(value = "SELECT EXISTS " +
            "(select * " +
            "FROM disliked_members d " +
            "WHERE d.member_id = :loginMemberId " +
            "AND d.disliked_member = :dislikedMemberId limit 1) as success", nativeQuery = true)
    int isExistDislikedMember(@Param("loginMemberId") Long loginMemberId,
                                  @Param("dislikedMemberId") Long dislikedMemberId);

    @Query(value = "SELECT EXISTS " +
            "(select * " +
            "FROM liked_members d " +
            "WHERE d.member_id = :loginMemberId " +
            "AND d.liked_member = :likedMemberId limit 1)", nativeQuery = true)
    int isExistLikedMember(@Param("loginMemberId") Long loginMemberId,
                              @Param("likedMemberId") Long likedMemberId);
}
