package idorm.idormServer.calendar.adapter.out.persistence;

import static idorm.idormServer.calendar.entity.QTeam.team;
import static idorm.idormServer.member.entity.QMember.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;

import org.springframework.stereotype.Component;

import idorm.idormServer.calendar.adapter.out.exception.NotFoundTeamException;
import idorm.idormServer.calendar.application.port.out.LoadTeamPort;
import idorm.idormServer.calendar.entity.Team;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoadTeamAdapter implements LoadTeamPort {

	private final JPAQueryFactory queryFactory;
	private final TeamRepository teamRepository;

	@Override
	public Optional<Team> findByMemberIdWithOptional(Long memberId) {
		Optional<Team> response = teamRepository.findByMembersId(memberId);
		return Optional.ofNullable(response).orElse(null);
	}

	@Override
	public Team findByMemberId(Long memberId) {
		Team response = queryFactory
				.select(team)
				.from(team)
				.join(team.members, member)
				.where(member.id.eq(memberId))
				.fetchOne();

		if(response == null) {
			throw new NotFoundTeamException();
		}
		return response;
	}
}
