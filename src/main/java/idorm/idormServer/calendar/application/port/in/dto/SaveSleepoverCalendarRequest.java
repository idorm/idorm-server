package idorm.idormServer.calendar.application.port.in.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import idorm.idormServer.calendar.entity.Period;
import idorm.idormServer.calendar.entity.SleepoverCalendar;
import idorm.idormServer.calendar.entity.Team;

public record SaveSleepoverCalendarRequest(
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	LocalDate startDate,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	LocalDate endDate
) {
	public SleepoverCalendar from(final Long memberId, final Team teamDomain) {
		return new SleepoverCalendar(new Period(startDate, endDate), memberId, teamDomain);
	}

	public Period periodOf() {
		return new Period(startDate, endDate);
	}
}