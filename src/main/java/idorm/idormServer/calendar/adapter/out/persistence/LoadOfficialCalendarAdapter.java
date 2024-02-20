package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.adapter.out.exception.NotFoundCalendarException;
import idorm.idormServer.calendar.application.port.out.LoadOfficialCalendarPort;
import idorm.idormServer.calendar.domain.OfficialCalendar;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadOfficialCalendarAdapter implements LoadOfficialCalendarPort {

  private final OfficialCalendarMapper officialCalendarMapper;
  private final OfficialCalendarRepository officialCalendarRepository;

  @Override
  public OfficialCalendar findById(Long officialCalendarId) {
    OfficialCalendarJpaEntity response = officialCalendarRepository.findById(officialCalendarId)
        .orElseThrow(NotFoundCalendarException::new);
    return officialCalendarMapper.toDomain(response);
  }

  @Override
  public List<OfficialCalendar> findByMonthByAdmin(String now, String lastWeek) {
    List<OfficialCalendarJpaEntity> responses = officialCalendarRepository.findByMonthByAdmin(now,
        lastWeek);
    return responses.isEmpty() ? new ArrayList<>() : officialCalendarMapper.toDomain(responses);
  }

  @Override
  public List<OfficialCalendar> findByMonthByMember(YearMonth yearMonth) {
    List<OfficialCalendarJpaEntity> responses = officialCalendarRepository.findByMonthByMember(yearMonth);
    return responses.isEmpty() ? new ArrayList<>() : officialCalendarMapper.toDomain(responses);
  }

  @Override
  public Boolean findByInuPostId(String inuPostId) {
    return officialCalendarRepository.existsByInuPostIdAndIsDeletedIsFalse(inuPostId);
  }
}
