package idorm.idormServer.calendar.application.port.in;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.calendar.application.port.in.dto.CalendarsResponse;
import idorm.idormServer.calendar.application.port.in.dto.FindSleepoverCalendarsRequest;
import idorm.idormServer.calendar.application.port.in.dto.SaveSleepoverCalendarRequest;
import idorm.idormServer.calendar.application.port.in.dto.SleepoverCalendarResponse;
import idorm.idormServer.calendar.application.port.in.dto.UpdateSleepoverCalendarRequest;

import java.util.List;

public interface SleepoverCalendarUseCase {

  SleepoverCalendarResponse save(AuthResponse authResponse, SaveSleepoverCalendarRequest request);

  SleepoverCalendarResponse update(AuthResponse authResponse, UpdateSleepoverCalendarRequest request);

  void delete(AuthResponse authResponse, Long teamCalendarId);

  SleepoverCalendarResponse findById(AuthResponse authResponse, Long teamCalendarId);

  List<SleepoverCalendarResponse> findSleepoverCalendarsByMonth(AuthResponse authResponse,
      FindSleepoverCalendarsRequest request);
}
