package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.application.port.out.SaveTeamCalendarPort;
import idorm.idormServer.calendar.domain.TeamCalendar;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveTeamCalendarAdapter implements SaveTeamCalendarPort {

    private final TeamCalendarMapper teamCalendarMapper;
    private final TeamCalendarRepository teamCalendarRepository;

    @Override
    public void saveTeamCalendar(TeamCalendar teamCalendar) {
        final TeamCalendarJpaEntity teamCalendarJpaEntity = teamCalendarMapper.toEntity(teamCalendar);
        teamCalendarRepository.save(teamCalendarJpaEntity);
    }
}