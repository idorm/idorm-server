package idorm.idormServer.matching.repository;

import idorm.idormServer.matching.domain.DislikedMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DislikedMemberRepository extends JpaRepository<DislikedMember, Long> {

    /**
     * MemberId로 싫어요한 멤버 아이디 리스트 반환하기
     */
    @Query(value = "SELECT selected_disliked_member_id " +
            "FROM disliked_member dm " +
            "WHERE dm.member_id = :memberId", nativeQuery = true)
    List<Long> findDislikedMembersByMemberId(@Param("memberId") Long memberId);

    /**
     * MemberId와 SelectedDislikedMemberId로 싫어요한 멤버 삭제하기
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM disliked_member " +
            "WHERE disliked_member.member_id = :memberId " +
            "AND disliked_member.selected_disliked_member_id = :selectedDislikedMemberId", nativeQuery = true)
    void deleteDislikedMember(@Param("memberId") Long memberId,
                           @Param("selectedDislikedMemberId") Long selectedMemberId);

    /**
     * MemberId와 싫어요한 멤버 식별자로 등록되었는지 확인하기. 존재한다면 Disliked 엔티티 식별자로 반환한다.
     * 좋아요한 매칭 멤버로 등록하기 전 체크 용도로 사용한다.
     */
    @Query(value = "SELECT disliked_member_id " +
            "FROM disliked_member dm " +
            "WHERE dm.member_id = :memberId AND " +
            "dm.selected_disliked_member_id = :dislikedMemberId", nativeQuery = true)
    Optional<Long> isRegisteredDislikedMemberIdByMemberId(@Param("memberId") Long memberId,
                                                          @Param("dislikedMemberId") Long dislikedMemberId);
}
