package idorm.idormServer.calendar.application.port.in.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import idorm.idormServer.calendar.domain.Participant;
import idorm.idormServer.calendar.domain.Period;
import idorm.idormServer.calendar.domain.SleepoverCalendar;
import idorm.idormServer.calendar.domain.Team;

public record SaveSleepoverCalendarRequest(
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	LocalDate startDate,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	LocalDate endDate
) {
	public SleepoverCalendar from(final Long memberId, final Team team) {
		return new SleepoverCalendar(
			new Period(startDate, endDate),
			new Participant(memberId),
			team);
	}

	public Period periodOf() {
		return new Period(startDate, endDate);
	}
}