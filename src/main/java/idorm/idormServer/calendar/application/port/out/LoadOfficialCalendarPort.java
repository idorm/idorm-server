package idorm.idormServer.calendar.application.port.out;

import java.time.YearMonth;
import java.util.List;

import idorm.idormServer.calendar.entity.OfficialCalendar;
import idorm.idormServer.matchingInfo.entity.DormCategory;

public interface LoadOfficialCalendarPort {

  OfficialCalendar findById(Long officialCalendarId);

  List<OfficialCalendar> findByToday(DormCategory dormCategory);

  List<OfficialCalendar> findByMonthByAdmin(String now, String lastWeek);

  List<OfficialCalendar> findByMonthByMember(YearMonth yearMonth);

  Boolean findByInuPostId(String inuPostId);

}
