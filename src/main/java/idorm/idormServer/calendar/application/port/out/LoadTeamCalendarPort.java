package idorm.idormServer.calendar.application.port.out;

import java.time.YearMonth;
import java.util.List;

import idorm.idormServer.calendar.entity.Team;
import idorm.idormServer.calendar.entity.TeamCalendar;
import idorm.idormServer.notification.adapter.out.api.NotificationRequest;

public interface LoadTeamCalendarPort {

	TeamCalendar findByIdAndTeamId(Long teamCalendarId, Long teamId);

	TeamCalendar findByIdAndMemberId(Long teamCalendarId, Long memberId);

	List<TeamCalendar> findByMemberId(Long memberId);

	List<TeamCalendar> findByYearMonth(Team team, YearMonth yearMonth);

	List<NotificationRequest> findByStartDateIsToday();
}