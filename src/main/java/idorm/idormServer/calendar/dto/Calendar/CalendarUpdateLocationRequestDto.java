package idorm.idormServer.calendar.dto.Calendar;

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
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({CalendarUpdateLocationRequestDto.class,
        ValidationSequence.NotNull.class,
        ValidationSequence.Size.class
})
@ApiModel(value = "일정 수정 요청 - 위치")
public class CalendarUpdateLocationRequestDto {

    @ApiModelProperty(position = 1, required = true, value="캘린더 식별자", example = "1")
    @Positive(message = "수정할 일정 식별자는 양수만 가능합니다.")
    @NotNull(message = "내용을 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private Long calendarId;

    @ApiModelProperty(position = 2, value = "장소", example = "3기숙사 1층")
    @Size(max = 50, message = "장소는 ~50자 이내여야 합니다.", groups = ValidationSequence.Size.class)
    private String location;
}
