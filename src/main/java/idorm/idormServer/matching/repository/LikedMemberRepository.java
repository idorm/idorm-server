package idorm.idormServer.matching.repository;

import idorm.idormServer.matching.domain.LikedMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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
    @Query(value = "DELETE " +
            "FROM liked_member lm " +
            "WHERE lm.member_id = :memberId AND " +
            "lm.selected_liked_member_id = :selectedLikedMemberId", nativeQuery = true)
    void deleteLikedMember(@Param("memberId") Long memberId,
                           @Param("selectedLikedMemberId") Long selectedMemberId);

}
