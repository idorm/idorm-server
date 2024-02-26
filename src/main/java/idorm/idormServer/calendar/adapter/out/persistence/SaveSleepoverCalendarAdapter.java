package idorm.idormServer.calendar.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.calendar.application.port.out.SaveSleepoverCalendarPort;
import idorm.idormServer.calendar.entity.SleepoverCalendar;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveSleepoverCalendarAdapter implements SaveSleepoverCalendarPort {

  private final SleepoverCalendarRepository sleepoverCalendarRepository;

  @Override
  public void save(SleepoverCalendar sleepoverCalendar) {
    sleepoverCalendarRepository.save(sleepoverCalendar);
  }
}
