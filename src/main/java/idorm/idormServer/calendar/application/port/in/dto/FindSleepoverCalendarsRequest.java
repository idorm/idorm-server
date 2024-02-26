package idorm.idormServer.calendar.application.port.in.dto;

import java.time.YearMonth;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public record FindSleepoverCalendarsRequest(
    @NotNull(message = "내용을 입력해 주세요.")
    @Positive(message = "조회할 회원 식별자는 양수만 가능합니다.")
    Long memberId,
    @NotNull(message = "년/월를 입력해 주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    @JsonProperty("yearMonth")
    YearMonth yearMonth
) {

}
