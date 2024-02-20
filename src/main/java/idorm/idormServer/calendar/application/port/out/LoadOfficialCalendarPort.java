package idorm.idormServer.calendar.application.port.out;

import idorm.idormServer.calendar.domain.OfficialCalendar;
import java.time.YearMonth;
import java.util.List;

public interface LoadOfficialCalendarPort {

  OfficialCalendar findById(Long officialCalendarId);

  List<OfficialCalendar> findByMonthByAdmin(String now, String lastWeek);

  List<OfficialCalendar> findByMonthByMember(YearMonth yearMonth);

  Boolean findByInuPostId(String inuPostId);

}
