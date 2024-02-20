package idorm.idormServer.calendar.application.port.in.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import idorm.idormServer.calendar.domain.Period;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(title = "팀 일정 수정 요청")
public record UpdateTeamCalendarRequest(
	@NotNull(message = "내용을 입력해 주세요.")
	Long teamCalendarId,
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
	@NotNull(message = "대상자의 식별자를 입력해주세요.")
	List<Long> targets
) {

	public Period getPeriod() {
		return new Period(startDate, endDate);
	}
}
