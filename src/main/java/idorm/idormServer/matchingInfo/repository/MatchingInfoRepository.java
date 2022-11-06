package idorm.idormServer.matchingInfo.repository;

import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchingInfoRepository extends JpaRepository<MatchingInfo, Long> {

    Optional<MatchingInfo> findByMemberId(Long memberId);

    @Query(value = "SELECT matching_info_id " +
            "FROM matching_info mi " +
            "WHERE mi.member_id = :memberId", nativeQuery = true)
    Optional<Long> findMatchingInfoIdByMemberId(@Param("memberId") Long memberId);

    @Query(value = "SELECT matching_info_id " +
            "FROM matching_info mi " +
            "WHERE mi.member_id != :memberId AND " +
            "mi.dorm_num = :dormNum AND " +
            "mi.join_period = :joinPeriod AND " +
            "mi.gender = :gender AND " +
            "mi.is_matching_info_public = 1", nativeQuery = true)
    List<Long> findMatchingMembers(@Param("memberId") Long memberId,
                                   @Param("dormNum") String dormNum,
                                   @Param("joinPeriod") String joinPeriod,
                                   @Param("gender") String gender);

    @Query(value = "SELECT matching_info_id " +
            "FROM matching_info mi " +
            "WHERE mi.member_id != :memberId AND " +
            "mi.dorm_num = :dormNum AND " +
            "mi.join_period = :joinPeriod AND " +
            "mi.gender = :gender AND " +
            "(mi.is_snoring = :isSnoring OR " +
            "mi.is_snoring = 0) AND " +
            "(mi.is_smoking = :isSmoking OR " +
            "mi.is_smoking = 0) AND " +
            "(mi.is_grinding = :isGrinding OR " +
            "mi.is_grinding = 0) AND " +
            "(mi.is_wear_earphones = :isWearEarphones OR " +
            "mi.is_wear_earphones = 1) AND " +
            "(mi.is_allowed_food = :isAllowedFood OR " +
            "mi.is_allowed_food = 0) AND " +
            "mi.age >= :minAge && mi.age <= :maxAge AND " +
            "mi.is_matching_info_public = 1", nativeQuery = true)
    List<Long> findFilteredMatchingMembers(@Param("memberId") Long memberId,
                                           @Param("dormNum") String dormNum,
                                           @Param("joinPeriod") String joinPeriod,
                                           @Param("gender") String gender,
                                           @Param("isSnoring") int isSnoring,
                                           @Param("isSmoking") int isSmoking,
                                           @Param("isGrinding") int isGrinding,
                                           @Param("isWearEarphones") int isWearEarphones,
                                           @Param("isAllowedFood") int isAllowedFood,
                                           @Param("minAge") Integer minAge,
                                           @Param("maxAge") Integer maxAge);
}