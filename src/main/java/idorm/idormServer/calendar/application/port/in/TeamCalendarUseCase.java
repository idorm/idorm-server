package idorm.idormServer.calendar.application.port.in;

import java.util.List;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.calendar.application.port.in.dto.FindOfficialCalendarsRequest;
import idorm.idormServer.calendar.application.port.in.dto.SaveTeamCalendarRequest;
import idorm.idormServer.calendar.application.port.in.dto.TeamCalendarResponse;
import idorm.idormServer.calendar.application.port.in.dto.UpdateTeamCalendarRequest;

public interface TeamCalendarUseCase {

	TeamCalendarResponse save(AuthResponse authResponse, SaveTeamCalendarRequest request);

	TeamCalendarResponse update(AuthResponse authResponse, UpdateTeamCalendarRequest request);

	void delete(AuthResponse authResponse, Long teamCalendarId);

	TeamCalendarResponse findById(AuthResponse authResponse, Long teamCalendarId);

	List<TeamCalendarResponse> findTeamCalendarsByMonth(AuthResponse authResponse, FindOfficialCalendarsRequest request);
}
