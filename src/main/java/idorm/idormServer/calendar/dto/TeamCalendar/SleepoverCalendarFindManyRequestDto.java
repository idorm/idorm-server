package idorm.idormServer.calendar.dto.TeamCalendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import idorm.idormServer.common.ValidationSequence;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
        SleepoverCalendarFindManyRequestDto.class,
        ValidationSequence.NotNull.class,
        ValidationSequence.Positive.class
})
@ApiModel(value = "외박 일정 월별 조회 요청")
public class SleepoverCalendarFindManyRequestDto {

    @ApiModelProperty(position = 1, required = true, value= "회원 식별자", example = "1")
    @NotNull(message = "내용을 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    @Positive(message = "조회할 회원 식별자는 양수만 가능합니다.")
    private Long memberId;

    @ApiModelProperty(position = 2, notes = "string", value = "조회할 년/월", example = "2023-04")
    @NotNull(message = "년/월를 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    private YearMonth yearMonth;
}
