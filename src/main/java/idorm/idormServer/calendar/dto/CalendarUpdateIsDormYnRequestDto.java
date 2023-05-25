package idorm.idormServer.calendar.dto;

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

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({CalendarUpdateIsDormYnRequestDto.class,
        ValidationSequence.NotNull.class
})
@ApiModel(value = "일정 수정 요청 - 기숙사 대상 여부")
public class CalendarUpdateIsDormYnRequestDto {

    @ApiModelProperty(position = 1, required = true, value="캘린더 식별자", example = "1")
    @Positive(message = "수정할 일정 식별자는 양수만 가능합니다.")
    @NotNull(message = "내용을 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private Long calendarId;

    @ApiModelProperty(position = 2, required = true, value = "1기숙사 대상 여부", allowableValues = "true, false",
            example = "true")
    @NotNull(message = "1기숙사 대상 여부를 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private Boolean isDorm1Yn;

    @ApiModelProperty(position = 3, required = true, value = "2기숙사 대상 여부", allowableValues = "true, false",
            example = "true")
    @NotNull(message = "2기숙사 대상 여부를 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private Boolean isDorm2Yn;

    @ApiModelProperty(position = 4, required = true, value = "3기숙사 대상 여부", allowableValues = "true, false",
            example = "true")
    @NotNull(message = "3기숙사 대상 여부를 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private Boolean isDorm3Yn;
}
