package idorm.idormServer.member.repository;

import idorm.idormServer.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    @Query(value = "SELECT matching_info_id " +
            "FROM matching_info mi " +
            "JOIN member m " +
            "ON mi.member_id=m.member_id " +
            "WHERE mi.member_id != :memberId AND " +
            "mi.dorm_num = :dormNum AND " +
            "mi.join_period = :joinPeriod AND " +
            "mi.gender = :gender AND " +
            "mi.is_matching_info_public = :isMatchingInfoPublic", nativeQuery = true)
    List<Long> findMatchingMembers(@Param("memberId") Long memberId,
                                           @Param("dormNum") String dormNum,
                                           @Param("joinPeriod") String joinPeriod,
                                           @Param("gender") String gender,
                                           @Param("isMatchingInfoPublic") Boolean isMatchingInfoPublic);
}
