package idorm.idormServer.calendar.adapter.out.persistence;

import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.calendar.adapter.out.exception.NotFoundSleepoverCalendarException;
import idorm.idormServer.calendar.application.port.out.LoadSleepoverCalendarPort;
import idorm.idormServer.calendar.entity.Period;
import idorm.idormServer.calendar.entity.SleepoverCalendar;
import idorm.idormServer.calendar.entity.Team;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadSleepoverCaldarAdapter implements LoadSleepoverCalendarPort {

  private final SleepoverCalendarRepository sleepoverCalendarRepository;

  @Override
  public SleepoverCalendar findById(Long sleepoverCalendarId) {
    SleepoverCalendar response = sleepoverCalendarRepository.findById(
            sleepoverCalendarId)
        .orElseThrow(NotFoundSleepoverCalendarException::new);
    return response;
  }

  @Override
  public SleepoverCalendar findByIdAndMemberId(Long sleepoverCalendarId,
      Long memberId) {
    SleepoverCalendar response = sleepoverCalendarRepository.findByIdAndMemberId(sleepoverCalendarId, memberId)
        .orElseThrow(NotFoundSleepoverCalendarException::new);
    return response;
  }

  @Override
  public List<SleepoverCalendar> findByToday(Team team) {
    List<SleepoverCalendar> responses = sleepoverCalendarRepository.findByToday(team.getId());
    return responses;
  }

  @Override
  public List<SleepoverCalendar> findByYearMonth(Team team,
      YearMonth yearMonth) {
    List<SleepoverCalendar> responses = sleepoverCalendarRepository.findByYearMonth(
        team.getId(), yearMonth.toString());
    return responses;
  }

  @Override
  public List<SleepoverCalendar> findByMemberId(Long memberId) {
    List<SleepoverCalendar> responses = sleepoverCalendarRepository.findByMemberId(
        memberId);
    return responses;
  }

  @Override
  public List<SleepoverCalendar> findByTeamId(Long teamId) {
    List<SleepoverCalendar> responses = sleepoverCalendarRepository.findByTeamId(teamId);
    return responses;
  }

  @Override
  public Long countOverlappingDates(Long memberId, Period period) {
    return sleepoverCalendarRepository.countOverlappingDates(memberId, period.getStartDate(),
        period.getEndDate());
  }
}
