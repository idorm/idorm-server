package idorm.idormServer.matchingInfo.adapter.out.persistence;

import static idorm.idormServer.matchingInfo.entity.QMatchingInfo.*;
import static idorm.idormServer.matchingMate.entity.QMatchingMate.*;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateResponse;
import idorm.idormServer.matchingMate.entity.MatePreferenceType;
import idorm.idormServer.member.entity.Member;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MatchingInfoRepositoryCustomImpl implements MatchingInfoRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<MatchingMateResponse> findByMatePreference(Member loginMember, MatePreferenceType preferenceType) {

		return queryFactory
			.select(Projections.constructor(MatchingMateResponse.class,
				matchingInfo,
				matchingInfo.member.id.as("memberId"))
			)
			.from(matchingInfo)
			.leftJoin(matchingInfo.member).fetchJoin()
			.leftJoin(matchingMate).on(matchingInfo.eq(matchingMate.matchingInfoForTarget))
			.where(matchingMate.preferenceType.eq(preferenceType),
				matchingMate.owner.eq(loginMember))
			.fetch();
	}
}
