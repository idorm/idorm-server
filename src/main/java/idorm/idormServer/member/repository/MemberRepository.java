package idorm.idormServer.member.repository;

import idorm.idormServer.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByIdAndIsDeletedIsFalse(Long id);

    boolean existsByNicknameAndIsDeletedIsFalse(String nickname);

    List<Member> findByDormCategoryAndIdIsNotAndIsDeletedIsFalseAndFcmTokenIsNotNull(Character dormCategory, Long id);

    @Query(value = "SELECT liked_member " +
            "FROM liked_members d " +
            "WHERE d.member_id = :memberId", nativeQuery = true)
    List<Long> findlikedMembersByLoginMemberId(@Param("memberId") Long memberId);

    @Query(value = "SELECT disliked_member " +
            "FROM disliked_members d " +
            "WHERE d.member_id = :memberId", nativeQuery = true)
    List<Long> findDislikedMembersByLoginMemberId(@Param("memberId") Long memberId);

    @Query(value = "SELECT EXISTS " +
            "(select * " +
            "FROM disliked_members d " +
            "WHERE d.member_id = :loginMemberId " +
            "AND d.disliked_member = :dislikedMemberId limit 1) as success", nativeQuery = true)
    int isExistDislikedMember(@Param("loginMemberId") Long loginMemberId,
                                  @Param("dislikedMemberId") Long dislikedMemberId);

    @Query(value = "SELECT EXISTS " +
            "(select * " +
            "FROM liked_members l " +
            "WHERE l.member_id = :loginMemberId " +
            "AND l.liked_member = :likedMemberId limit 1)", nativeQuery = true)
    int isExistLikedMember(@Param("loginMemberId") Long loginMemberId,
                              @Param("likedMemberId") Long likedMemberId);

    @Modifying
    @Transactional
    @Query(value = "DELETE " +
            "FROM liked_members " +
            "WHERE liked_members.member_id = :memberId || " +
            "liked_members.liked_member = :memberId", nativeQuery = true)
    void deleteAllLikedMembersByDeletedMember(@Param("memberId") Long deletedMemberId);

    @Modifying
    @Transactional
    @Query(value = "DELETE " +
            "FROM disliked_members " +
            "WHERE disliked_members.member_id = :memberId || " +
            "disliked_members.disliked_member = :memberId", nativeQuery = true)
    void deleteAllDislikedMembersByDeletedMember(@Param("memberId") Long deletedMemberId);
}
