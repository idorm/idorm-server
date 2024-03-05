package idorm.idormServer.calendar.application.port.in.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import idorm.idormServer.calendar.entity.Period;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "팀 일정 수정 요청")
public record UpdateTeamCalendarRequest(
    @Positive(message = "teamCalendarId , 수정할 팀 일정 식별자는 양수만 가능합니다.")
    Long teamCalendarId,
    @NotNull(message = "일정 제목은 공백일 수 없습니다.")
    @Size(min = 1, max = 30, message = "title , 제목은 1~30자 이내여야 합니다.")
    String title,
    @NotBlank(message = "일정 내용은 공백일 수 없습니다.")
    @Size(min = 1, max = 100, message = "content , 내용은 1~100자 이내여야 합니다.")
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
