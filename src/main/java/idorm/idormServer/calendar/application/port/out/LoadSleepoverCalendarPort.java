package idorm.idormServer.calendar.application.port.out;

import idorm.idormServer.calendar.entity.Period;
import idorm.idormServer.calendar.entity.SleepoverCalendar;
import idorm.idormServer.calendar.entity.Team;
import java.time.YearMonth;
import java.util.List;

public interface LoadSleepoverCalendarPort {

  SleepoverCalendar findById(Long sleepoverCalendarId);

  SleepoverCalendar findByIdAndMemberId(Long sleepoverCalendarId, Long memberId);

  List<SleepoverCalendar> findByMemberId(Long memberId);

  List<SleepoverCalendar> findByToday(Team team);

  List<SleepoverCalendar> findByYearMonth(Team team, YearMonth yearMonth);


  void hasOverlappingDatesWithSleepoverId(Long memberId, Period period, Long sleepoverCalendarId);
  void hasOverlappingDatesWithoutSleepoverId(Long memberId, Period period);
}
