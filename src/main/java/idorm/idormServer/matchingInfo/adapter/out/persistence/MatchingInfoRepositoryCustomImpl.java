package idorm.idormServer.matchingInfo.adapter.out.persistence;

import static idorm.idormServer.matchingInfo.entity.QMatchingInfo.*;
import static idorm.idormServer.matchingMate.entity.QMatchingMate.*;
import static idorm.idormServer.member.entity.QMember.*;
import static org.springframework.util.StringUtils.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import idorm.idormServer.matchingInfo.entity.DormCategory;
import idorm.idormServer.matchingInfo.entity.Gender;
import idorm.idormServer.matchingInfo.entity.JoinPeriod;
import idorm.idormServer.matchingInfo.entity.MatchingInfo;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateFilterRequest;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateResponse;
import idorm.idormServer.matchingMate.application.port.in.dto.QMatchingMateResponse;
import idorm.idormServer.matchingMate.entity.MatePreferenceType;
import idorm.idormServer.member.entity.Member;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MatchingInfoRepositoryCustomImpl implements MatchingInfoRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<MatchingMateResponse> findByMatePreference(final Member loginMember,
		final MatePreferenceType preferenceType, Pageable pageable) {

		List<MatchingMateResponse> content = queryFactory
			.select(new QMatchingMateResponse(
				matchingInfo,
				matchingInfo.member.id.as("memberId"))
			)
			.from(matchingInfo)
			.leftJoin(matchingInfo.member).fetchJoin()
			.leftJoin(matchingMate).on(matchingInfo.eq(matchingMate.matchingInfoForTarget))
			.where(matchingMate.preferenceType.eq(preferenceType),
				matchingMate.owner.eq(loginMember))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<MatchingInfo> countQuery = queryFactory
			.selectFrom(matchingInfo)
			.leftJoin(matchingMate).on(matchingInfo.eq(matchingMate.matchingInfoForTarget))
			.where(matchingMate.preferenceType.eq(preferenceType),
				matchingMate.owner.eq(loginMember));

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
	}

	@Override
	public Page<MatchingMateResponse> findByBasicConditions(final Member loginMember, final Pageable pageable) {
		List<MatchingMateResponse> content = queryFactory
			.select(Projections.constructor(MatchingMateResponse.class,
				matchingInfo,
				matchingInfo.member.id.as("memberId"))
			)
			.from(matchingInfo)
			.leftJoin(matchingInfo.member, member).fetchJoin()
			.where(basicCond(loginMember.getId(), loginMember.getMatchingInfo()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<MatchingInfo> countQuery = queryFactory
			.selectFrom(matchingInfo)
			.where(basicCond(loginMember.getId(), loginMember.getMatchingInfo()));

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
	}

	@Override
	public Page<MatchingMateResponse> findBySpecialConditions(final Member loginMember,
		final MatchingMateFilterRequest request, final Pageable pageable) {

		List<MatchingMateResponse> content = queryFactory
			.select(Projections.constructor(MatchingMateResponse.class,
				matchingInfo,
				matchingInfo.member.id.as("memberId"))
			)
			.from(matchingInfo)
			.leftJoin(matchingInfo.member, member).fetchJoin()
			.where(specialCond(loginMember.getId(), loginMember.getMatchingInfo(), request))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<MatchingInfo> countQuery = queryFactory
			.selectFrom(matchingInfo)
			.where(specialCond(loginMember.getId(), loginMember.getMatchingInfo(), request));

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
	}

	private BooleanExpression basicCond(Long memberId, MatchingInfo matchingInfo) {
		return matchingPublic()
			.and(memberNotEq(memberId))
			.and(dormEq(matchingInfo.getDormInfo().getDormCategory()))
			.and(joinPeriodEq(matchingInfo.getDormInfo().getJoinPeriod()))
			.and(genderEq(matchingInfo.getDormInfo().getGender()));
	}

	private BooleanExpression specialCond(Long memberId, MatchingInfo matchingInfo, MatchingMateFilterRequest request) {
		return basicCond(memberId, matchingInfo)
			.and(isSnoringEq(request.isSnoring()))
			.and(isSmokingEq(request.isSmoking()))
			.and(isGrindingEq(request.isGrinding()))
			.and(isWearEarphonesEq(request.isWearEarphones()))
			.and(isAllowedFoodEq(request.isAllowedFood()))
			.and(ageGoe(request.minAge()))
			.and(ageLoe(request.maxAge()));
	}

	private BooleanExpression matchingPublic() {
		return matchingInfo.isPublic.eq(true);
	}

	private BooleanExpression memberNotEq(Long memberId) {
		return isEmpty(memberId) ? null : member.id.ne(memberId);
	}

	private BooleanExpression dormEq(DormCategory dormCategory) {
		return isEmpty(dormCategory) ? null : matchingInfo.dormInfo.dormCategory.eq(dormCategory);
	}

	private BooleanExpression joinPeriodEq(JoinPeriod joinPeriod) {
		return isEmpty(joinPeriod) ? null : matchingInfo.dormInfo.joinPeriod.eq(joinPeriod);
	}

	private BooleanExpression genderEq(Gender gender) {
		return isEmpty(gender) ? null : matchingInfo.dormInfo.gender.eq(gender);
	}

	private BooleanExpression isSnoringEq(Boolean isSnoring) {
		return isEmpty(isSnoring) ? null : matchingInfo.preferenceInfo.isSnoring.ne(isSnoring);
	}

	private BooleanExpression isSmokingEq(Boolean isSmoking) {
		return isEmpty(isSmoking) ? null : matchingInfo.preferenceInfo.isSmoking.ne(isSmoking);
	}

	private BooleanExpression isGrindingEq(Boolean isGrinding) {
		return isEmpty(isGrinding) ? null : matchingInfo.preferenceInfo.isGrinding.ne(isGrinding);
	}

	private BooleanExpression isWearEarphonesEq(Boolean isWearEarphones) {
		return isEmpty(isWearEarphones) ? null : matchingInfo.preferenceInfo.isWearEarphones.ne(isWearEarphones);
	}

	private BooleanExpression isAllowedFoodEq(Boolean isAllowedFood) {
		return isEmpty(isAllowedFood) ? null : matchingInfo.preferenceInfo.isAllowedFood.ne(isAllowedFood);
	}

	private BooleanExpression ageGoe(Integer ageGoe) {
		return isEmpty(ageGoe) ? null : matchingInfo.preferenceInfo.age.goe(ageGoe);
	}

	private BooleanExpression ageLoe(Integer ageLoe) {
		return isEmpty(ageLoe) ? null : matchingInfo.preferenceInfo.age.loe(ageLoe);
	}
}
