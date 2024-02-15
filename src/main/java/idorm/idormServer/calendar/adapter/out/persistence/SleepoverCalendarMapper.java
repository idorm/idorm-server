package idorm.idormServer.calendar.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.calendar.domain.SleepoverCalendar;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SleepoverCalendarMapper {

	private final TeamMapper teamMapper;
	private final PeriodMapper periodMapper;
	private final ParticipantMapper participantMapper;

	public SleepoverCalendarJpaEntity toEntity(SleepoverCalendar sleepoverCalendar) {

		return new SleepoverCalendarJpaEntity(sleepoverCalendar.getId(),
			periodMapper.toEntity(sleepoverCalendar.getPeriod()),
			participantMapper.toEntity(sleepoverCalendar.getParticipant()),
			teamMapper.toEntity(sleepoverCalendar.getTeam()));
	}

	public List<SleepoverCalendarJpaEntity> toEntity(List<SleepoverCalendar> sleepoverCalendars) {
		List<SleepoverCalendarJpaEntity> result = sleepoverCalendars.stream()
			.map(this::toEntity)
			.toList();
		return result;
	}

	public SleepoverCalendar toDomain(SleepoverCalendarJpaEntity sleepoverCalendarJpaEntity) {
		return SleepoverCalendar.forMapper(sleepoverCalendarJpaEntity.getId(),
			periodMapper.toDomain(sleepoverCalendarJpaEntity.getPeriod()),
			participantMapper.toDomain(sleepoverCalendarJpaEntity.getParticipant()),
			teamMapper.toDomain(sleepoverCalendarJpaEntity.getTeam()));
	}

	public List<SleepoverCalendar> toDomain(List<SleepoverCalendarJpaEntity> entities) {
		List<SleepoverCalendar> result = entities.stream()
			.map(this::toDomain)
			.toList();
		return result;
	}
}