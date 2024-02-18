package idorm.idormServer.matchingInfo.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingInfoRepository extends JpaRepository<MatchingInfoJpaEntity, Long> {

	Optional<MatchingInfoJpaEntity> findByMemberId(Long memberId);

	boolean existsByMemberId(Long memberId);

	// TODO : MatchingMate 리팩 시 수정
	// List<MatchingInfoJpaEntity> findAllByMemberIdNotAndDormCategoryAndJoinPeriodAndGenderAndIsMatchingInfoPublicTrueAndIsDeletedIsFalse(
	//         Long memberId,
	//         Character dormCategory,
	//         Character joinPeriod,
	//         Character gender);

	// @Query(value = "SELECT m FROM MatchingInfo m " +
	//         "WHERE m.member.id <> :memberId AND " +
	//         "m.dormCategory = :dormCategory AND " +
	//         "m.joinPeriod = :joinPeriod AND " +
	//         "m.gender = :gender AND " +
	//         "(m.isSnoring = :isSnoring OR m.isSnoring = false) AND " +
	//         "(m.isSmoking = :isSmoking OR m.isSmoking = false) AND " +
	//         "(m.isGrinding = :isGrinding OR m.isGrinding = false) AND " +
	//         "(m.isWearEarphones = :isWearEarphones OR m.isWearEarphones = false) AND " +
	//         "(m.isAllowedFood = :isAllowedFood OR m.isAllowedFood = false) AND " +
	//         "m.age >= :minAge AND " +
	//         "m.age <= :maxAge AND " +
	//         "m.isMatchingInfoPublic = true")
	// List<MatchingInfoJpaEntity> findFilteredMatchingMembers(@Param("memberId") Long memberId,
	//                                                @Param("dormCategory") Character dormCategory,
	//                                                @Param("joinPeriod") Character joinPeriod,
	//                                                @Param("gender") Character gender,
	//                                                @Param("isSnoring") Boolean isSnoring,
	//                                                @Param("isSmoking") Boolean isSmoking,
	//                                                @Param("isGrinding") Boolean isGrinding,
	//                                                @Param("isWearEarphones") Boolean isWearEarphones,
	//                                                @Param("isAllowedFood") Boolean isAllowedFood,
	//                                                @Param("minAge") Integer minAge,
	//                                                @Param("maxAge") Integer maxAge);
}