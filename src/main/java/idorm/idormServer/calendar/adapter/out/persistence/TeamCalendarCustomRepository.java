package idorm.idormServer.calendar.adapter.out.persistence;

import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Repository;

import idorm.idormServer.calendar.entity.Team;
import idorm.idormServer.calendar.entity.TeamCalendar;
import idorm.idormServer.notification.adapter.out.api.NotificationRequest;

@Repository
public interface TeamCalendarCustomRepository {

	TeamCalendar findByIdAndMemberId(Long teamCalendarId, Long memberId);

	List<TeamCalendar> findByMemberId(Long memberId);

	List<TeamCalendar> findByYearMonth(Team teamDomain, YearMonth yearMonth);

	List<NotificationRequest> findByStartDateIsToday();
}