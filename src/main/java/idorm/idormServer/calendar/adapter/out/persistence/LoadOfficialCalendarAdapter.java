package idorm.idormServer.calendar.adapter.out.persistence;

import static idorm.idormServer.calendar.entity.QOfficialCalendar.officialCalendar;

import com.querydsl.jpa.impl.JPAQueryFactory;
import idorm.idormServer.calendar.adapter.out.exception.NotFoundCalendarException;
import idorm.idormServer.calendar.application.port.out.LoadOfficialCalendarPort;
import idorm.idormServer.calendar.entity.OfficialCalendar;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import idorm.idormServer.matchingInfo.entity.DormCategory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadOfficialCalendarAdapter implements LoadOfficialCalendarPort {

  private final JPAQueryFactory queryFactory;
  private final OfficialCalendarRepository officialCalendarRepository;

  @Override
  public OfficialCalendar findById(Long officialCalendarId) {
    OfficialCalendar response = officialCalendarRepository.findById(officialCalendarId)
        .orElseThrow(NotFoundCalendarException::new);
    return response;
  }

  @Override
  public List<OfficialCalendar> findByToday(DormCategory dormCategory) {
    List<OfficialCalendar> responses = queryFactory
        .select(officialCalendar)
        .from(officialCalendar)
        .where(officialCalendar.period.startDate.eq(LocalDate.now()))
        .fetch();
    return responses.isEmpty() ? new ArrayList<>() : responses;
  }

  @Override
  public List<OfficialCalendar> findByMonthByAdmin(String now, String lastWeek) {
    List<OfficialCalendar> responses = officialCalendarRepository.findByMonthByAdmin(now, lastWeek);
    return responses.isEmpty() ? new ArrayList<>() : responses;
  }

  @Override
  public List<OfficialCalendar> findByMonthByMember(YearMonth yearMonth) {
    List<OfficialCalendar> responses = officialCalendarRepository.findByMonthByMember(yearMonth);
    return responses.isEmpty() ? new ArrayList<>() : responses;
  }

  @Override
  public Boolean findByInuPostId(String inuPostId) {
    return officialCalendarRepository.existsByInuPostIdAndIsDeletedIsFalse(inuPostId);
  }
}
