package idorm.idormServer.calendar.application.port.in.dto;

import java.time.YearMonth;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public record FindOfficialCalendarsRequest(
	@NotNull(message = "년/월를 입력해 주세요.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
	YearMonth yearMonth
) {
}