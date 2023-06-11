package idorm.idormServer.calendar.dto.TeamCalendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import idorm.idormServer.calendar.domain.TeamCalendar;
import idorm.idormServer.calendar.dto.Team.TeamMemberFindResponseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "팀 일정 요약 응답")
public class TeamCalendarAbstractResponseDto {

    @ApiModelProperty(position = 1, required = true, value = "팀일정 식별자", example = "1")
    private Long teamCalendarId;

    @ApiModelProperty(position = 2, required = true, value = "내용", example = "청소")
    private String title;

    @ApiModelProperty(position = 4, notes = "string", value = "시작일자", example = "2023-04-27")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty(position = 8, required = true, value = "일정 대상자의 식별자")
    private List<TeamMemberFindResponseDto> targets = new ArrayList<>();

    @Builder
    public TeamCalendarAbstractResponseDto(TeamCalendar teamCalendar, List<TeamMemberFindResponseDto> targets) {
        this.teamCalendarId = teamCalendar.getId();
        this.title = teamCalendar.getTitle();

        if (teamCalendar.getStartDate() != null)
            this.startDate = teamCalendar.getStartDate();

        if (targets != null)
            this.targets = targets.stream().collect(Collectors.toList());
    }
}
