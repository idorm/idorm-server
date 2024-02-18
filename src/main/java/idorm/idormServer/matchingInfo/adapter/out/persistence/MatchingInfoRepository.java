package idorm.idormServer.matchingInfo.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.matchingInfo.domain.Gender;
import idorm.idormServer.matchingInfo.domain.JoinPeriod;

public interface MatchingInfoRepository extends JpaRepository<MatchingInfoJpaEntity, Long> {

	Optional<MatchingInfoJpaEntity> findByMemberId(Long memberId);

	boolean existsByMemberId(Long memberId);

	List<MatchingInfoJpaEntity> findAllByMemberIdNotAndDormCategoryAndJoinPeriodAndGenderAndIsMatchingInfoPublicTrue(
		Long memberId,
		DormCategory dormCategory,
		JoinPeriod joinPeriod,
		Gender gender);

	@Query(value = "SELECT m FROM MatchingInfoJpaEntity m " +
		"WHERE m.member.id <> :memberId AND " +
		"m.dormInfo.dormCategory = :dormCategory AND " +
		"m.dormInfo.joinPeriod = :joinPeriod AND " +
		"m.dormInfo.gender = :gender AND " +
		"(m.preferenceInfo.isSnoring = :isSnoring OR m.preferenceInfo.isSnoring = false) AND " +
		"(m.preferenceInfo.isSmoking = :isSmoking OR m.preferenceInfo.isSmoking = false) AND " +
		"(m.preferenceInfo.isGrinding = :isGrinding OR m.preferenceInfo.isGrinding = false) AND " +
		"(m.preferenceInfo.isWearEarphones = :isWearEarphones OR m.preferenceInfo.isWearEarphones = false) AND " +
		"(m.preferenceInfo.isAllowedFood = :isAllowedFood OR m.preferenceInfo.isAllowedFood = false) AND " +
		"m.preferenceInfo.age >= :minAge AND " +
		"m.preferenceInfo.age <= :maxAge AND " +
		"m.isPublic = true")
	List<MatchingInfoJpaEntity> findFilteredMates(@Param("memberId") Long memberId,
	                                               @Param("dormCategory") DormCategory dormCategory,
	                                               @Param("joinPeriod") JoinPeriod joinPeriod,
	                                               @Param("gender") Gender gender,
	                                               @Param("isSnoring") Boolean isSnoring,
	                                               @Param("isSmoking") Boolean isSmoking,
	                                               @Param("isGrinding") Boolean isGrinding,
	                                               @Param("isWearEarphones") Boolean isWearEarphones,
	                                               @Param("isAllowedFood") Boolean isAllowedFood,
	                                               @Param("minAge") Integer minAge,
	                                               @Param("maxAge") Integer maxAge);
}