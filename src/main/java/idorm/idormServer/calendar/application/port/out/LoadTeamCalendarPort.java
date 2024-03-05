package idorm.idormServer.calendar.application.port.out;

import java.time.YearMonth;
import java.util.List;

import idorm.idormServer.calendar.entity.Team;
import idorm.idormServer.calendar.entity.TeamCalendar;

public interface LoadTeamCalendarPort {

  TeamCalendar findByIdAndTeamId(Long teamCalendarId, Long teamId);

  TeamCalendar findByIdAndMemberId(Long teamCalendarId, Long memberId);

  List<TeamCalendar> findByMemberId(Long memberId);

  List<TeamCalendar> findByTeamId(Long teamId);

  List<TeamCalendar> findByYearMonth(Team team, YearMonth yearMonth);

  /**
   * TODO: 쿼리 조건
   * 1. 시작 일자가 오늘이어야 한다.
   * 2. 삭제 되지 않은 일정이어야 한다.
   * 3. 일정 대상자가 존재해야 한다.
   */
  List<TeamCalendar> findByStartDateIsToday();
}
