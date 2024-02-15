package idorm.idormServer.calendar.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.calendar.domain.TeamCalendar;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamCalendarMapper {

	private final PeriodMapper periodMapper;
	private final DurationMapper durationMapper;
	private final TitleMapper titleMapper;
	private final ContentMapper contentMapper;
	private final ParticipantsMapper participantsMapper;
	private final TeamMapper teamMapper;

	public TeamCalendarJpaEntity toEntity(TeamCalendar teamCalendar) {

		return new TeamCalendarJpaEntity(teamCalendar.getId(),
			periodMapper.toEntity(teamCalendar.getPeriod()),
			durationMapper.toEntity(teamCalendar.getDuration()),
			titleMapper.toEntity(teamCalendar.getTitle()),
			contentMapper.toEntity(teamCalendar.getContent()),
			participantsMapper.toEntity(teamCalendar.getParticipants()),
			teamMapper.toEntity(teamCalendar.getTeam()));
	}

	public List<TeamCalendarJpaEntity> toEntity(List<TeamCalendar> teamCalendars) {
		List<TeamCalendarJpaEntity> result = teamCalendars.stream()
			.map(this::toEntity)
			.toList();
		return result;
	}

	public TeamCalendar toDomain(TeamCalendarJpaEntity teamCalendarJpaEntity) {
		return TeamCalendar.forMapper(teamCalendarJpaEntity.getId(),
			periodMapper.toDomain(teamCalendarJpaEntity.getPeriod()),
			durationMapper.toDomain(teamCalendarJpaEntity.getDuration()),
			titleMapper.toDomain(teamCalendarJpaEntity.getTitle()),
			contentMapper.toDomain(teamCalendarJpaEntity.getContent()),
			participantsMapper.toDomain(teamCalendarJpaEntity.getParticipants()),
			teamMapper.toDomain(teamCalendarJpaEntity.getTeam()));
	}

	public List<TeamCalendar> toDomain(List<TeamCalendarJpaEntity> entities) {
		List<TeamCalendar> result = entities.stream()
			.map(this::toDomain)
			.toList();
		return result;
	}
}