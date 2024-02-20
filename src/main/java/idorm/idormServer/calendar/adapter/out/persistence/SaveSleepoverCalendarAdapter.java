package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.application.port.out.SaveSleepoverCalendarPort;
import idorm.idormServer.calendar.domain.SleepoverCalendar;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveSleepoverCalendarAdapter implements SaveSleepoverCalendarPort {

  private final SleepoverCalendarMapper sleepoverCalendarMapper;
  private final SleepoverCalendarRepository sleepoverCalendarRepository;

  @Override
  public void save(SleepoverCalendar sleepoverCalendar) {
    SleepoverCalendarJpaEntity sleepoverCalendarJpaEntity = sleepoverCalendarMapper.toEntity(
        sleepoverCalendar);
    sleepoverCalendarRepository.save(sleepoverCalendarJpaEntity);
  }
}
