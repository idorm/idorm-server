package idorm.idormServer.calendar.dto.Calendar;

import idorm.idormServer.common.ValidationSequence;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({CalendarUpdateContentRequestDto.class,
        ValidationSequence.NotBlank.class,
        ValidationSequence.NotNull.class,
        ValidationSequence.Positive.class
})
@ApiModel(value = "일정 수정 요청 - 내용")
public class CalendarUpdateContentRequestDto {

    @ApiModelProperty(position = 1, required = true, value="캘린더 식별자", example = "1")
    @Positive(message = "수정할 일정 식별자는 양수만 가능합니다.", groups = ValidationSequence.Positive.class)
    @NotNull(message = "일정 식별자를 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private Long calendarId;

    @ApiModelProperty(position = 2, required = true, example = "기숙사 화재 훈련", value = "내용")
    @NotBlank(message = "내용을 입력해 주세요.", groups = ValidationSequence.NotBlank.class)
    private String content;
}
