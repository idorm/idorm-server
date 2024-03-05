package idorm.idormServer.calendar.application.port.in.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import idorm.idormServer.calendar.entity.Duration;
import idorm.idormServer.calendar.entity.Participant;
import idorm.idormServer.calendar.entity.Participants;
import idorm.idormServer.calendar.entity.Period;
import idorm.idormServer.calendar.entity.Team;
import idorm.idormServer.calendar.entity.TeamCalendar;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record SaveTeamCalendarRequest(
    @NotBlank(message = "일정 제목은 공백일 수 없습니다.")
    @Size(min =1, max = 30, message = "title , 제목은 1~30자 이내여야 합니다.")
    String title,
    @NotBlank(message = "일정 내용은 공백일 수 없습니다.")
    @Size(min =1, max = 100, message = "content , 내용은 1~100자 이내여야 합니다.")
    String content,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate startDate,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate endDate,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    LocalTime startTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    LocalTime endTime,
    Set<Participant> participants
) {

  public TeamCalendar toEntity(final Team team) {
    return new TeamCalendar(title, content, new Period(startDate, endDate), new Duration(startTime, endTime),
        new Participants(participants), team
    );
  }
}
