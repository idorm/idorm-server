package idorm.idormServer.calendar.application.port.in.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import idorm.idormServer.calendar.domain.Content;
import idorm.idormServer.calendar.domain.Duration;
import idorm.idormServer.calendar.domain.Participant;
import idorm.idormServer.calendar.domain.Participants;
import idorm.idormServer.calendar.domain.Period;
import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.calendar.domain.TeamCalendar;
import idorm.idormServer.calendar.domain.Title;

public record SaveTeamCalendarRequest(
	@NotBlank(message = "내용을 입력해 주세요.")
	@Size(max = 15, message = "제목은 ~15자 이내여야 합니다.")
	String title,
	@Size(max = 100, message = "내용은 ~100자 이내여야 합니다.")
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

	public TeamCalendar from(final Team team) {
		return new TeamCalendar(
			new Title(title),
			new Content(content),
			new Period(startDate, endDate),
			new Duration(startTime, endTime),
			new Participants(participants),
			team
		);
	}

}
