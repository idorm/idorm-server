package idorm.idormServer.member.repository;

import idorm.idormServer.matchingInfo.domain.Dormitory;
import idorm.idormServer.matchingInfo.domain.Gender;
import idorm.idormServer.matchingInfo.domain.JoinPeriod;
import idorm.idormServer.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    //    @Query(value = "SELECT * " +
//            "FROM member m " +
//            "JOIN m.matchinginfo i " +
//            "WHERE m.id != :memberId AND " +
//            "i.dormNum = :dormNum AND " +
//            "i.joinPeriod = :joinPeriod AND " +
//            "i.gender = :gender", nativeQuery = true)
    @Query(value = "SELECT * " +
            "FROM member m " +
            "JOIN m.matching_info mi " +
            "WHERE m.id != :memberId AND " +
            "m.dorm_num = :dormNum AND " +
            "m.join_period = :joinPeriod AND " +
            "m.gender = :gender", nativeQuery = true)
    List<Member> findMatchingMembers(@Param("memberId") Long memberId,
                                     @Param("dormNum") Dormitory dormNum,
                                     @Param("joinPeriod") JoinPeriod joinPeriod,
                                     @Param("gender") Gender gender);
}
