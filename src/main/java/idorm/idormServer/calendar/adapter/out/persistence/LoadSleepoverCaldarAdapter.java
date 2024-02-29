package idorm.idormServer.calendar.adapter.out.persistence;

import static idorm.idormServer.calendar.entity.QSleepoverCalendar.sleepoverCalendar;
import static idorm.idormServer.calendar.entity.QTeam.team;

import com.querydsl.jpa.impl.JPAQueryFactory;
import idorm.idormServer.calendar.adapter.out.exception.DuplicatedSleepoverDateException;
import idorm.idormServer.calendar.adapter.out.exception.NotFoundSleepoverCalendarException;
import idorm.idormServer.calendar.application.port.out.LoadSleepoverCalendarPort;
import idorm.idormServer.calendar.entity.Period;
import idorm.idormServer.calendar.entity.SleepoverCalendar;
import idorm.idormServer.calendar.entity.Team;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadSleepoverCaldarAdapter implements LoadSleepoverCalendarPort {

  private final JPAQueryFactory queryFactory;
  private final SleepoverCalendarRepository sleepoverCalendarRepository;

  @Override
  public SleepoverCalendar findById(Long sleepoverCalendarId) {
    SleepoverCalendar response = sleepoverCalendarRepository.findById(sleepoverCalendarId)
        .orElseThrow(NotFoundSleepoverCalendarException::new);
    return response;
  }

  @Override
  public SleepoverCalendar findByIdAndMemberId(Long sleepoverCalendarId, Long memberId) {
    SleepoverCalendar response = queryFactory
        .select(sleepoverCalendar)
        .from(sleepoverCalendar)
        .where(sleepoverCalendar.id.eq(sleepoverCalendarId)
            .and(sleepoverCalendar.memberId.eq((memberId))))
        .fetchOne();
    if(response == null) {
      throw new NotFoundSleepoverCalendarException();
    }
    return response;
  }

  @Override
  public List<SleepoverCalendar> findByToday(Team teamDomain) {
    List<SleepoverCalendar> responses = queryFactory
        .select(sleepoverCalendar)
        .from(sleepoverCalendar)
        .join(sleepoverCalendar.team, team)
        .where(team.eq(teamDomain)
            .and(sleepoverCalendar.period.startDate.eq(LocalDate.now())))
        .fetch();
    return responses.isEmpty() ? new ArrayList<>() : responses;
  }

  @Override
  public List<SleepoverCalendar> findByYearMonth(Team teamDomain, YearMonth yearMonth) {
    List<SleepoverCalendar> responses = queryFactory
        .select(sleepoverCalendar)
        .from(sleepoverCalendar)
        .join(sleepoverCalendar.team, team)
        .where(team.eq(teamDomain)
            .and(sleepoverCalendar.period.startDate.month().eq(yearMonth.getMonthValue()))
            .or(sleepoverCalendar.period.endDate.month().eq(yearMonth.getMonthValue())))
        .fetch();
    return responses.isEmpty() ? new ArrayList<>() : responses;
  }

  @Override
  public List<SleepoverCalendar> findByMemberId(Long memberId) {
    List<SleepoverCalendar> responses = sleepoverCalendarRepository.findByMemberId(
        memberId);
    return responses.isEmpty() ? new ArrayList<>() : responses;
  }

  @Override
  public void hasOverlappingDatesWithSleepoverId(Long memberId, Period period, Long sleepoverCalendarId) {
    Long count = queryFactory
        .select(sleepoverCalendar)
        .from(sleepoverCalendar)
        .where(sleepoverCalendar.period.endDate.goe(period.getStartDate()),
            sleepoverCalendar.period.startDate.loe(period.getEndDate()),
                sleepoverCalendar.id.ne(sleepoverCalendarId)
                .and(sleepoverCalendar.memberId.eq(memberId)))
        .fetchCount();
    if(count > 0 ) {
      throw new DuplicatedSleepoverDateException();
    }
  }

  @Override
  public void hasOverlappingDatesWithoutSleepoverId(Long memberId, Period period) {
    Long count = queryFactory
        .select(sleepoverCalendar)
        .from(sleepoverCalendar)
        .where(sleepoverCalendar.period.endDate.goe(period.getStartDate()),
            sleepoverCalendar.period.startDate.loe(period.getEndDate())
                .and(sleepoverCalendar.memberId.eq(memberId)))
        .fetchCount();
    if(count > 0 ) {
      throw new DuplicatedSleepoverDateException();
    }
  }

}
