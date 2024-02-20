package idorm.idormServer.calendar.application.port.in.dto;

import java.util.List;

public record CalendarsResponse(
    List<TeamCalendarResponse> teamCalendarResponses,
    List<SleepoverCalendarResponse> sleepoverCalendarResponses
) {
  public static CalendarsResponse of(final List<TeamCalendarResponse> teamCalendars,
      final List<SleepoverCalendarResponse> sleepoverCalendars) {
    return new CalendarsResponse(teamCalendars, sleepoverCalendars);
  }
}
