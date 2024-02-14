package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.domain.SleepoverCalendar;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

    public SleepoverCalendar toDomain(SleepoverCalendarJpaEntity sleepoverCalendarJpaEntity) {
        return SleepoverCalendar.forMapper(sleepoverCalendarJpaEntity.getId(),
                periodMapper.toDomain(sleepoverCalendarJpaEntity.getPeriod()),
                participantMapper.toDomain(sleepoverCalendarJpaEntity.getParticipant()),
                teamMapper.toDomain(sleepoverCalendarJpaEntity.getTeam()));
    }
}