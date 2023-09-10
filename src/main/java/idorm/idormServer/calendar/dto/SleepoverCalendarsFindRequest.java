package idorm.idormServer.calendar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import idorm.idormServer.common.ValidationSequence;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.YearMonth;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({
        SleepoverCalendarsFindRequest.class,
        ValidationSequence.NotNull.class,
        ValidationSequence.Positive.class
})
@Schema(title = "외박 일정 월별 조회 요청")
public class SleepoverCalendarsFindRequest {

    @Schema(required = true, description= "회원 식별자", example = "1")
    @NotNull(message = "내용을 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    @Positive(message = "조회할 회원 식별자는 양수만 가능합니다.")
    private Long memberId;

    @Schema(name = "yearMonth", format = "string", description = "조회할 년/월", example = "2023-04")
    @NotNull(message = "년/월를 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    @JsonProperty("yearMonth")
    private YearMonth yearMonth;
}
