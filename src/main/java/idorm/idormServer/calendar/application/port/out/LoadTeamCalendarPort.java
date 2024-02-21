package idorm.idormServer.calendar.application.port.out;

import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.calendar.domain.TeamCalendar;
import java.time.YearMonth;
import java.util.List;

public interface LoadTeamCalendarPort {

  TeamCalendar findByIdAndTeamId(Long teamCalendarId, Long teamId);
  TeamCalendar findByIdAndMemberId(Long teamCalendarId, Long memberId);
  List<TeamCalendar> findByMemberId(Long memberId);
  List<TeamCalendar> findByTeamId(Long teamId);
  List<TeamCalendar> findByYearMonth(Team team, YearMonth yearMonth);
}
