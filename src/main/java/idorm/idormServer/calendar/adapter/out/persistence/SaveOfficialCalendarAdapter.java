package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.application.port.out.SaveOfficialCalendarPort;
import idorm.idormServer.calendar.domain.OfficialCalendar;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveOfficialCalendarAdapter implements SaveOfficialCalendarPort {

  private final OfficialCalendarMapper officialCalendarMapper;
  private final OfficialCalendarRepository officialCalendarRepository;

  @Override
  public void save(OfficialCalendar officialCalendar) {
    officialCalendarRepository.save(officialCalendarMapper.toEntity(officialCalendar));
  }
}
