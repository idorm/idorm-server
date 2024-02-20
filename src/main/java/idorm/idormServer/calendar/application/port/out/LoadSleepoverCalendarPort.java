package idorm.idormServer.calendar.application.port.out;


import idorm.idormServer.calendar.domain.Period;
import idorm.idormServer.calendar.domain.SleepoverCalendar;
import idorm.idormServer.calendar.domain.Team;
import java.time.YearMonth;
import java.util.List;

public interface LoadSleepoverCalendarPort {

  SleepoverCalendar findById(Long sleepoverCalendarId);
  SleepoverCalendar findByIdAndMemberId(Long sleepoverCalendarId, Long memberId);

  List<SleepoverCalendar> findByMemberId(Long memberId);

  List<SleepoverCalendar> findByTeamId(Long teamId);

  List<SleepoverCalendar> findByToday(Team team);

  List<SleepoverCalendar> findByYearMonth(Team team, YearMonth yearMonth);


  Long countOverlappingDates(Long memberId, Period period);
}
