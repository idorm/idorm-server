package idorm.idormServer.calendar.application.port.in.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import idorm.idormServer.calendar.domain.SleepoverCalendar;

public record SleepoverCalendarResponse(
	Long sleepoverCalendarId,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	LocalDate startDate,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	LocalDate endDate,
	SleepoverCalendarParticipantResponse targets
) {

	public static SleepoverCalendarResponse of(final SleepoverCalendar sleepoverCalendar,
		final SleepoverCalendarParticipantResponse target) {
		return new SleepoverCalendarResponse(
			sleepoverCalendar.getId(),
			sleepoverCalendar.getPeriod().getStartDate(),
			sleepoverCalendar.getPeriod().getEndDate(),
			target);
	}
}
