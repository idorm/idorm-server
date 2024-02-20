package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.adapter.out.exception.NotFoundSleepoverCalendarException;
import idorm.idormServer.calendar.application.port.out.LoadSleepoverCalendarPort;
import idorm.idormServer.calendar.domain.Period;
import idorm.idormServer.calendar.domain.SleepoverCalendar;
import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.calendar.domain.TeamCalendar;
import java.time.YearMonth;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadSleepoverCaldarAdapter implements LoadSleepoverCalendarPort {

  private final SleepoverCalendarMapper sleepoverCalendarMapper;
  private final SleepoverCalendarRepository sleepoverCalendarRepository;

  @Override
  public SleepoverCalendar findById(Long sleepoverCalendarId) {
    SleepoverCalendarJpaEntity response = sleepoverCalendarRepository.findById(
            sleepoverCalendarId)
        .orElseThrow(NotFoundSleepoverCalendarException::new);
    return sleepoverCalendarMapper.toDomain(response);
  }

  @Override
  public SleepoverCalendar findByIdAndMemberId(Long sleepoverCalendarId, Long memberId) {
    SleepoverCalendarJpaEntity response = sleepoverCalendarRepository.findByIdAndMemberId(sleepoverCalendarId, memberId)
        .orElseThrow(NotFoundSleepoverCalendarException::new);
    return sleepoverCalendarMapper.toDomain(response);
    }

  @Override
  public List<SleepoverCalendar> findByToday(Team team) {
    List<SleepoverCalendarJpaEntity> responses = sleepoverCalendarRepository.findByToday(
        team.getId());
    return sleepoverCalendarMapper.toDomain(responses);
  }

  @Override
  public List<SleepoverCalendar> findByYearMonth(Team team, YearMonth yearMonth) {
    List<SleepoverCalendarJpaEntity> responses = sleepoverCalendarRepository.findByYearMonth(
        team.getId(), yearMonth.toString());
    return sleepoverCalendarMapper.toDomain(responses);
  }

  @Override
  public List<SleepoverCalendar> findByMemberId(Long memberId) {
    List<SleepoverCalendarJpaEntity> responses = sleepoverCalendarRepository.findByMemberId(
        memberId);
    return sleepoverCalendarMapper.toDomain(responses);
  }

  @Override
  public List<SleepoverCalendar> findByTeamId(Long teamId) {
    List<SleepoverCalendarJpaEntity> responses = sleepoverCalendarRepository.findByTeamId(teamId);
    return sleepoverCalendarMapper.toDomain(responses);
  }

  @Override
  public Long countOverlappingDates(Long memberId, Period period) {
    return sleepoverCalendarRepository.countOverlappingDates(memberId, period.getStartDate(),
        period.getEndDate());
  }
}
