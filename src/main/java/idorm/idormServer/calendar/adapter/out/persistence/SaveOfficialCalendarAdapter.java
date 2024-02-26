package idorm.idormServer.calendar.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.calendar.application.port.out.SaveOfficialCalendarPort;
import idorm.idormServer.calendar.entity.OfficialCalendar;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveOfficialCalendarAdapter implements SaveOfficialCalendarPort {

  private final OfficialCalendarRepository officialCalendarRepository;

  @Override
  public void save(OfficialCalendar officialCalendar) {
    officialCalendarRepository.save(officialCalendar);
  }
}
