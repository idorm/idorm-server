package idorm.idormServer.calendar.application.port.in.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import idorm.idormServer.calendar.entity.Duration;
import idorm.idormServer.calendar.entity.Participant;
import idorm.idormServer.calendar.entity.Participants;
import idorm.idormServer.calendar.entity.Period;
import idorm.idormServer.calendar.entity.Team;
import idorm.idormServer.calendar.entity.TeamCalendar;

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
	public TeamCalendar toEntity(final Team team) {
		return new TeamCalendar(title, content, new Period(startDate, endDate), new Duration(startTime, endTime),
			new Participants(participants), team
		);
	}
}
