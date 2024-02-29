package idorm.idormServer.matchingInfo.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import idorm.idormServer.matchingInfo.entity.DormCategory;
import idorm.idormServer.matchingInfo.entity.Gender;
import idorm.idormServer.matchingInfo.entity.JoinPeriod;
import idorm.idormServer.matchingInfo.entity.MatchingInfo;

public interface MatchingInfoRepository extends JpaRepository<MatchingInfo, Long> {

	Optional<MatchingInfo> findByMemberId(Long memberId);

	boolean existsByMemberId(Long memberId);

	@Query(value = "SELECT m "
		+ "FROM MatchingInfo m "
		+ "WHERE m.dormInfo.dormCategory = :dormCategory "
		+ "AND m.dormInfo.joinPeriod = :joinPeriod "
		+ "AND m.dormInfo.gender = :gender "
		+ "AND m.isPublic = true "
		+ "AND m.member.id != :memberId")
	List<MatchingInfo> findAllByMemberIdNotAndDormCategoryAndJoinPeriodAndGenderAndIsMatchingInfoPublicTrue(
		@Param("memberId") Long memberId,
		@Param("dormCategory") DormCategory dormCategory,
		@Param("joinPeriod") JoinPeriod joinPeriod,
		@Param("gender") Gender gender);

	@Query(value = "SELECT m FROM MatchingInfo m " +
		"WHERE m.id != :id AND " +
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
	List<MatchingInfo> findFilteredMates(@Param("id") Long id,
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