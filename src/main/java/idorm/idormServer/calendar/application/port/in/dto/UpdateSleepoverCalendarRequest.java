package idorm.idormServer.calendar.application.port.in.dto;

import idorm.idormServer.calendar.domain.Period;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateSleepoverCalendarRequest(
	@NotNull(message = "내용을 입력해 주세요.")
	@Positive(message = "수정할 팀 일정 식별자는 양수만 가능합니다.")
	@JsonProperty("teamCalendarId")
	Long teamCalendarId,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonProperty("startDate")
	LocalDate startDate,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonProperty("endDate")
	LocalDate endDate
) {
	public Period period(){
		return new Period(startDate, endDate);
	}
}
