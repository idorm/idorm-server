package idorm.idormServer.calendar.application.port.in;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.calendar.application.port.in.dto.CrawledOfficialCalendarResponse;
import idorm.idormServer.calendar.application.port.in.dto.FindOfficialCalendarsRequest;
import idorm.idormServer.calendar.application.port.in.dto.OfficialCalendarResponse;
import idorm.idormServer.calendar.application.port.in.dto.OfficialCalendarUpdateRequest;

import java.util.List;

public interface OfficialCalendarUseCase {

	OfficialCalendarResponse update(OfficialCalendarUpdateRequest request);

	void delete(Long officialCalendarId);

	List<CrawledOfficialCalendarResponse> findByMonthByAdmin();

	OfficialCalendarResponse findOneByAdmin(Long officialCalendarId);

	List<OfficialCalendarResponse> findByMonthByMember(AuthResponse authResponse, FindOfficialCalendarsRequest request);
}
