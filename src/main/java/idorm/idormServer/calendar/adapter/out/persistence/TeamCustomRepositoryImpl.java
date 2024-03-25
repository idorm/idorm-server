package idorm.idormServer.calendar.adapter.out.persistence;

import static idorm.idormServer.calendar.entity.QTeam.*;
import static idorm.idormServer.member.entity.QMember.*;

import com.querydsl.jpa.impl.JPAQueryFactory;

import idorm.idormServer.calendar.entity.Team;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TeamCustomRepositoryImpl implements TeamCustomRepository {

	JPAQueryFactory queryFactory;

	@Override
	public Team findByMemberId(final Long memberId) {
		return queryFactory
			.select(team)
			.from(team)
			.join(team.members, member)
			.where(member.id.eq(memberId))
			.fetchOne();
	}
}