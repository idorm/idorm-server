package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.application.port.out.DeleteTeamCalendarPort;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteTeamCalendarAdapter implements DeleteTeamCalendarPort {

    @Override
    public void deleteTeamCalendar(Long teamCalendarId) {

    }
}
