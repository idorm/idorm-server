package idorm.idormServer.calendar.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.member.adapter.out.persistence.MemberMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMapper {

	private final MemberMapper memberMapper;
	private final TeamCalendarMapper teamCalendarMapper;
	private final SleepoverCalendarMapper sleepoverCalendarMapper;

	public TeamJpaEntity toEntity(Team team) {

		return new TeamJpaEntity(team.getId(),
			team.getTeamStatus(),
			memberMapper.toEntity(team.getMembers()),
			teamCalendarMapper.toEntity(team.getTeamCalendars()),
			sleepoverCalendarMapper.toEntity(team.getSleepoverCalendars()),
			team.getCreatedAt());
	}

	public Team toDomain(TeamJpaEntity teamJpaEntity) {
		return Team.forMapper(teamJpaEntity.getId(),
			teamJpaEntity.getTeamStatus(),
			memberMapper.toDomain(teamJpaEntity.getMembers()),
			teamCalendarMapper.toDomain(teamJpaEntity.getTeamCalendars()),
			sleepoverCalendarMapper.toDomain(teamJpaEntity.getSleepoverCalendars()),
			teamJpaEntity.getCreatedAt());
	}
}