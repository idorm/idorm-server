package idorm.idormServer.matching.repository;

import idorm.idormServer.matching.domain.LikedMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface LikedMemberRepository extends JpaRepository<LikedMember, Long> {

    /**
     * MemberId로 좋아요한 멤버 아이디 리스트 반환하기
     */
    @Query(value = "SELECT selected_liked_member_id " +
            "FROM liked_member lm " +
            "WHERE lm.member_id = :memberId", nativeQuery = true)
    List<Long> findLikedMembersByMemberId(@Param("memberId") Long memberId);

    /**
     * MemberId와 SelectedLikedMemberId로 좋아요한 멤버 삭제하기
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM liked_member " +
            "WHERE liked_member.member_id = :memberId " +
            "AND liked_member.selected_liked_member_id = :selectedLikedMemberId", nativeQuery = true)
    void deleteLikedMember(@Param("memberId") Long memberId,
                           @Param("selectedLikedMemberId") Long selectedMemberId);

    /**
     * MemberId와 좋아요한 멤버 식별자로 등록되었는지 확인하기. 존재한다면 LikedMember 엔티티 식별자로 반환한다.
     * 싫어요 매칭 멤버로 등록하기 전 체크 용도로 사용한다.
     */
    @Query(value = "SELECT liked_member_id " +
            "FROM liked_member lm " +
            "WHERE lm.member_id = :memberId AND " +
            "lm.selected_liked_member_id = :likedMemberId", nativeQuery = true)
    Optional<Long> isRegisteredLikedMemberIdByMemberId(@Param("memberId") Long memberId,
                                                          @Param("likedMemberId") Long likedMemberId);

    /**
     * MemberId 혹은 SelectedLikedMemberId에 해당 멤버 식별자가 들어있다면 삭제한다. 이는 회원 탈퇴 시 사용한다.
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM liked_member " +
            "WHERE liked_member.member_id = :memberId " +
            "OR liked_member.selected_liked_member_id = :memberId", nativeQuery = true)
    void deleteLikedMembers(@Param("memberId") Long memberId);

}
