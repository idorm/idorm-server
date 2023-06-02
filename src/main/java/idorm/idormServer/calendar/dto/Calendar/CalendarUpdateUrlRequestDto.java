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
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({CalendarUpdateUrlRequestDto.class,
        ValidationSequence.NotNull.class,
        ValidationSequence.Size.class
})
@ApiModel(value = "일정 수정 요청 - 링크")
public class CalendarUpdateUrlRequestDto {

    @ApiModelProperty(position = 1, required = true, value="캘린더 식별자", example = "1")
    @Positive(message = "수정할 일정 식별자는 양수만 가능합니다.")
    @NotNull(message = "내용을 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private Long calendarId;
    @ApiModelProperty(position = 2, value = "참고용 웹 링크",
            example = "https://www.inu.ac.kr/user/indexMain.do?command=&siteId=dorm")
    @Size(max = 300, message = "링크는 ~300자 이내여야 합니다.", groups = ValidationSequence.Size.class)
    private String url;
}
