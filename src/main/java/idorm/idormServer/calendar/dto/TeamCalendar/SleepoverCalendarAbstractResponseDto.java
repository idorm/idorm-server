package idorm.idormServer.calendar.dto.TeamCalendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import idorm.idormServer.calendar.domain.TeamCalendar;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "외박 일정 요약 응답")
public class SleepoverCalendarAbstractResponseDto {

    @ApiModelProperty(position = 1, required = true, value = "외박 일정 식별자", example = "1")
    private Long teamCalendarId;

    @ApiModelProperty(position = 2, notes = "string", value = "시작일자", example = "2023-04-27")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty(position = 3, notes = "string", value = "종료일자", example = "2023-04-27")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Builder
    public SleepoverCalendarAbstractResponseDto(TeamCalendar teamCalendar) {
        this.teamCalendarId = teamCalendar.getId();

        if (teamCalendar.getStartDate() != null)
            this.startDate = teamCalendar.getStartDate();
        if (teamCalendar.getEndDate() != null)
            this.endDate = teamCalendar.getEndDate();
    }
}
