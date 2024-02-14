package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.domain.TeamCalendar;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

    public TeamCalendar toDomain(TeamCalendarJpaEntity teamCalendarJpaEntity) {
        return TeamCalendar.forMapper(teamCalendarJpaEntity.getId(),
                periodMapper.toDomain(teamCalendarJpaEntity.getPeriod()),
                durationMapper.toDomain(teamCalendarJpaEntity.getDuration()),
                titleMapper.toDomain(teamCalendarJpaEntity.getTitle()),
                contentMapper.toDomain(teamCalendarJpaEntity.getContent()),
                participantsMapper.toDomain(teamCalendarJpaEntity.getParticipants()),
                teamMapper.toDomain(teamCalendarJpaEntity.getTeam()));
    }
}