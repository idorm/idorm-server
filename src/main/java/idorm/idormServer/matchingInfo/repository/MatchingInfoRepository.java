package idorm.idormServer.matchingInfo.repository;

import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchingInfoRepository extends JpaRepository<MatchingInfo, Long> {

    Optional<MatchingInfo> findByMemberId(Long memberId);

    List<MatchingInfo> findAllByMemberIdNotAndDormCategoryAndJoinPeriodAndGenderAndIsMatchingInfoPublicTrue(
                                                                              Long memberId,
                                                                              Character dormCategory,
                                                                              Character joinPeriod,
                                                                              Character gender);

    @Query(value = "SELECT m FROM MatchingInfo m " +
            "WHERE m.member.id <> :memberId AND " +
            "m.dormCategory = :dormCategory AND " +
            "m.joinPeriod = :joinPeriod AND " +
            "m.gender = :gender AND " +
            "(m.isSnoring = :isSnoring OR m.isSnoring = false) AND " +
            "(m.isSmoking = :isSmoking OR m.isSmoking = false) AND " +
            "(m.isGrinding = :isGrinding OR m.isGrinding = false) AND " +
            "m.isWearEarphones = :isWearEarphones AND " +
            "m.isAllowedFood = :isAllowedFood AND " +
            "m.age >= :minAge AND " +
            "m.age <= :maxAge AND " +
            "m.isMatchingInfoPublic = true")
    List<MatchingInfo> findFilteredMatchingMembers(@Param("memberId") Long memberId,
                                                   @Param("dormCategory") Character dormCategory,
                                                   @Param("joinPeriod") Character joinPeriod,
                                                   @Param("gender") Character gender,
                                                   @Param("isSnoring") Boolean isSnoring,
                                                   @Param("isSmoking") Boolean isSmoking,
                                                   @Param("isGrinding") Boolean isGrinding,
                                                   @Param("isWearEarphones") Boolean isWearEarphones,
                                                   @Param("isAllowedFood") Boolean isAllowedFood,
                                                   @Param("minAge") Integer minAge,
                                                   @Param("maxAge") Integer maxAge);
}