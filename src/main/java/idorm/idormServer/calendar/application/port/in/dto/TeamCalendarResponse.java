package idorm.idormServer.calendar.application.port.in.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import idorm.idormServer.calendar.entity.TeamCalendar;

public record TeamCalendarResponse(
    Long teamCalendarId,
    String title,
    String content,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate startDate,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate endDate,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    LocalTime startTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    LocalTime endTime,
    List<TeamCalendarParticipantResponse> targets
) {

  public static TeamCalendarResponse of(final TeamCalendar teamCalendar,
      List<TeamCalendarParticipantResponse> targets) {
    return new TeamCalendarResponse(
        teamCalendar.getId(),
        teamCalendar.getTitle(),
        teamCalendar.getContent(),
        teamCalendar.getPeriod().getStartDate(),
        teamCalendar.getPeriod().getEndDate(),
        teamCalendar.getDuration().getStartTime(),
        teamCalendar.getDuration().getEndTime(),
        targets);
  }
}
