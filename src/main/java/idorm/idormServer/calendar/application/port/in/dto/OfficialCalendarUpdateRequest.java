package idorm.idormServer.calendar.application.port.in.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.Positive;

public record OfficialCalendarUpdateRequest(
	@NotNull(message = "공식 일정 식별자를 입력해 주세요.")
	@Positive(message = "calendarId , 공식 일정 식별자는 양수만 가능합니다.")
	Long calendarId,
	@NotNull(message = "1기숙사 대상 여부를 입력해 주세요.")
	Boolean isDorm1Yn,
	@NotNull(message = "2기숙사 대상 여부를 입력해 주세요.")
	Boolean isDorm2Yn,
	@NotNull(message = "3기숙사 대상 여부를 입력해 주세요.")
	Boolean isDorm3Yn,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	LocalDate startDate,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	LocalDate endDate,
	@NotBlank(message = "제목을 입력해 주세요.")
	String title
) {

}

