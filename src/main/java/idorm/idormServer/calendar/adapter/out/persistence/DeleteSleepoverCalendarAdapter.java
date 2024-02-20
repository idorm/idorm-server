package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.application.port.out.DeleteSleepoverCalendarPort;
import idorm.idormServer.calendar.domain.SleepoverCalendar;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteSleepoverCalendarAdapter implements DeleteSleepoverCalendarPort {

  private final SleepoverCalendarMapper sleepoverCalendarMapper;
  private final SleepoverCalendarRepository sleepoverCalendarRepository;

  @Override
  public void delete(SleepoverCalendar sleepoverCalendar) {
    sleepoverCalendarRepository.delete(sleepoverCalendarMapper.toEntity(sleepoverCalendar));
  }
}
