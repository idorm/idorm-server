package idorm.idormServer.calendar.dto.TeamCalendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.calendar.domain.TeamCalendar;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.GroupSequence;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({SleepoverCalendarDefaultRequestDto.class,
})
@ApiModel(value = "외박일정 기본 요청")
public class SleepoverCalendarDefaultRequestDto {

    @ApiModelProperty(position = 1, notes = "string", value = "시작일자", example = "2023-04-27")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty(position = 2, notes = "string", value = "종료일자", example = "2023-04-28")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public TeamCalendar toEntity(Team team, Long target) {
        return TeamCalendar.builder()
                .team(team)
                .targets(new ArrayList<>(Arrays.asList(target)))
                .isSleepover(true)
                .title("외박")
                .startDate(this.startDate)
                .endDate(this.endDate)
                .build();
    }
}