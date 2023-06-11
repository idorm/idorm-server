package idorm.idormServer.calendar.dto.Calendar;

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
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({CalendarUpdateStartAndEndTimeRequestDto.class,
        ValidationSequence.NotNull.class,
})
@ApiModel(value = "일정 수정 요청 - 시작 및 종료 시간")
public class CalendarUpdateStartAndEndTimeRequestDto {

    @ApiModelProperty(position = 1, required = true, value="캘린더 식별자", example = "1")
    @Positive(message = "수정할 일정 식별자는 양수만 가능합니다.")
    @NotNull(message = "내용을 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private Long calendarId;

    @ApiModelProperty(position = 2, notes = "string", value = "시작시간", example = "15:40:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime startTime;

    @ApiModelProperty(position = 3, notes = "string", value = "종료시간", example = "16:50:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime endTime;
}
