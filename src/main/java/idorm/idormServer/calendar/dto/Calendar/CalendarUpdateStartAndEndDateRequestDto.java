package idorm.idormServer.calendar.dto;

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
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({CalendarUpdateStartAndEndDateRequestDto.class,
        ValidationSequence.NotNull.class,
})
@ApiModel(value = "일정 수정 요청 - 시작 및 종료일자")
public class CalendarUpdateStartAndEndDateRequestDto {

    @ApiModelProperty(position = 1, required = true, value="캘린더 식별자", example = "1")
    @Positive(message = "수정할 일정 식별자는 양수만 가능합니다.")
    @NotNull(message = "내용을 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private Long calendarId;

    @ApiModelProperty(position = 2, notes = "string", value = "시작일자", example = "2023-04-27")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty(position = 3, notes = "string", value = "종료일자", example = "2023-04-28")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
