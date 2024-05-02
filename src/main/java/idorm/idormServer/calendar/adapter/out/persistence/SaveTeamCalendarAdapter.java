package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.application.port.out.SaveTeamCalendarPort;
import idorm.idormServer.calendar.entity.TeamCalendar;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveTeamCalendarAdapter implements SaveTeamCalendarPort {

    private final TeamCalendarRepository teamCalendarRepository;

    @Override
    public void save(TeamCalendar teamCalendar) {
        teamCalendarRepository.save(teamCalendar);
    }
}