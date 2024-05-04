package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.entity.Period;
import idorm.idormServer.calendar.entity.SleepoverCalendar;
import idorm.idormServer.calendar.entity.Team;

import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface SleepoverCalendarCustomRepository {

	SleepoverCalendar findByIdAndMemberId(Long sleepoverCalendarId, Long memberId);

	List<SleepoverCalendar> findByToday(Team team);

	List<SleepoverCalendar> findByYearMonth(Team teamDomain, YearMonth yearMonth);

	Long hasOverlappingDatesWithSleepoverId(Long memberId, Period period, Long sleepoverCalendarId);

	Long hasOverlappingDatesWithoutSleepoverId(Long memberId, Period period);
}