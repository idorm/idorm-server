package idorm.idormServer.calendar.adapter.out.persistence;

import static idorm.idormServer.calendar.entity.QSleepoverCalendar.*;
import static idorm.idormServer.calendar.entity.QTeam.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import idorm.idormServer.calendar.entity.Period;
import idorm.idormServer.calendar.entity.SleepoverCalendar;
import idorm.idormServer.calendar.entity.Team;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SleepoverCalendarCustomRepositoryImpl implements SleepoverCalendarCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public SleepoverCalendar findByIdAndMemberId(Long sleepoverCalendarId, Long memberId) {
		return queryFactory
			.select(sleepoverCalendar)
			.from(sleepoverCalendar)
			.where(sleepoverCalendar.id.eq(sleepoverCalendarId)
				.and(sleepoverCalendar.memberId.eq((memberId))))
			.fetchOne();
	}

	@Override
	public List<SleepoverCalendar> findByToday(Team teamDomain) {
		return queryFactory
			.select(sleepoverCalendar)
			.from(sleepoverCalendar)
			.join(sleepoverCalendar.team, team)
			.where(team.eq(teamDomain)
				.and(sleepoverCalendar.period.startDate.eq(LocalDate.now())))
			.fetch();
	}

	@Override
	public List<SleepoverCalendar> findByYearMonth(Team teamDomain, YearMonth yearMonth) {
		return queryFactory
			.select(sleepoverCalendar)
			.from(sleepoverCalendar)
			.join(sleepoverCalendar.team, team)
			.where(team.eq(teamDomain)
				.and(sleepoverCalendar.period.startDate.month().eq(yearMonth.getMonthValue()))
				.or(sleepoverCalendar.period.endDate.month().eq(yearMonth.getMonthValue())))
			.fetch();
	}

	@Override
	public Long hasOverlappingDatesWithSleepoverId(Long memberId, Period period, Long sleepoverCalendarId) {
		return queryFactory
			.select(sleepoverCalendar)
			.from(sleepoverCalendar)
			.where(sleepoverCalendar.period.endDate.goe(period.getStartDate()),
				sleepoverCalendar.period.startDate.loe(period.getEndDate()),
				sleepoverCalendar.id.ne(sleepoverCalendarId)
					.and(sleepoverCalendar.memberId.eq(memberId)))
			.fetchCount();
	}

	@Override
	public Long hasOverlappingDatesWithoutSleepoverId(Long memberId, Period period) {
		return queryFactory
			.select(sleepoverCalendar)
			.from(sleepoverCalendar)
			.where(sleepoverCalendar.period.endDate.goe(period.getStartDate()),
				sleepoverCalendar.period.startDate.loe(period.getEndDate())
					.and(sleepoverCalendar.memberId.eq(memberId)))
			.fetchCount();
	}
}
