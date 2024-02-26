package idorm.idormServer.calendar.application.port.in.dto;

import java.time.LocalDate;

import idorm.idormServer.calendar.entity.OfficialCalendar;


public record OfficialCalendarResponse(
    Long calendarId,
    Boolean isDorm1Yn,
    Boolean isDorm2Yn,
    Boolean isDorm3Yn,
    LocalDate startDate,
    LocalDate endDate,
    String title,
    String postUrl,
    LocalDate inuPostCreatedAt

) {

  public static OfficialCalendarResponse from(final OfficialCalendar calendar) {
    return new OfficialCalendarResponse(calendar.getId(),
        calendar.getIsDorm1Yn(),
        calendar.getIsDorm2Yn(),
        calendar.getIsDorm3Yn(),
        calendar.getPeriod().getStartDate(),
        calendar.getPeriod().getEndDate(),
        calendar.getTitle(),
        calendar.getInuPostUrl(),
        calendar.getInuPostCreatedAt());
  }
}
