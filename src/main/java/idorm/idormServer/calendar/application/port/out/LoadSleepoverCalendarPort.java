package idorm.idormServer.calendar.application.port.out;

import java.time.YearMonth;
import java.util.List;

import idorm.idormServer.calendar.entity.Period;
import idorm.idormServer.calendar.entity.SleepoverCalendar;
import idorm.idormServer.calendar.entity.Team;

public interface LoadSleepoverCalendarPort {

  SleepoverCalendar findById(Long sleepoverCalendarId);
  SleepoverCalendar findByIdAndMemberId(Long sleepoverCalendarId, Long memberId);

  List<SleepoverCalendar> findByMemberId(Long memberId);

  List<SleepoverCalendar> findByTeamId(Long teamId);

  List<SleepoverCalendar> findByToday(Team team);

  List<SleepoverCalendar> findByYearMonth(Team team, YearMonth yearMonth);


  Long countOverlappingDates(Long memberId, Period period);
}
