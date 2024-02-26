package idorm.idormServer.calendar.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.calendar.application.port.out.DeleteSleepoverCalendarPort;
import idorm.idormServer.calendar.entity.SleepoverCalendar;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteSleepoverCalendarAdapter implements DeleteSleepoverCalendarPort {

  private final SleepoverCalendarRepository sleepoverCalendarRepository;

  @Override
  public void delete(SleepoverCalendar sleepoverCalendar) {
    sleepoverCalendarRepository.delete(sleepoverCalendar);
  }
}
