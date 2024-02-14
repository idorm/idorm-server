package idorm.idormServer.calendar.application;

import idorm.idormServer.calendar.application.port.in.TeamCalendarUseCase;
import idorm.idormServer.calendar.application.port.in.dto.RoomMateCalendarSaveRequest;
import idorm.idormServer.calendar.application.port.in.dto.RoomMateCalendarUpdateRequest;
import idorm.idormServer.calendar.application.port.out.DeleteTeamCalendarPort;
import idorm.idormServer.calendar.application.port.out.LoadTeamPort;
import idorm.idormServer.calendar.application.port.out.SaveTeamCalendarPort;
import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.calendar.domain.TeamCalendar;
import idorm.idormServer.member.domain.Member;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamCalendarService implements TeamCalendarUseCase {

    private final SaveTeamCalendarPort saveTeamCalendarPort;
    private final DeleteTeamCalendarPort deleteTeamCalendarPort;
    private final LoadTeamPort loadTeamPort;

    @Override
    public void save(Member member, RoomMateCalendarSaveRequest request) {
        Team team = loadTeamPort.load(member);
        TeamCalendar teamCalendar = request.toDomain(team);

        saveTeamCalendarPort.saveTeamCalendar(teamCalendar);
    }

    @Override
    public void findById(Long teamCalendarId) {

    }

    @Override
    public void findAllByMonth(YearMonth month) {

    }

    @Override
    public void update(RoomMateCalendarUpdateRequest request) {

    }

    @Override
    public void delete(Long teamCalendarId) {

    }
}
