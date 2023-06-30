package idorm.idormServer.calendar.dto.TeamCalendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import idorm.idormServer.calendar.domain.TeamCalendar;
import idorm.idormServer.calendar.dto.Team.TeamMemberFindResponseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "팀 일정 기본 응답")
public class TeamCalendarDefaultResponseDto {

    @ApiModelProperty(position = 1, required = true, value = "팀일정 식별자", example = "1")
    private Long teamCalendarId;

    @ApiModelProperty(position = 2, required = true, value = "내용", example = "청소")
    private String title;

    @ApiModelProperty(position = 3, value = "내용", example = "방 청소만 하는게 아니라 화장실거울까지!")
    private String content;

    @ApiModelProperty(position = 4, notes = "string", value = "시작일자", example = "2023-04-27")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty(position = 5, notes = "string", value = "종료일자", example = "2023-04-28")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ApiModelProperty(position = 6, notes = "string", value = "시작시간", example = "15:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime startTime;

    @ApiModelProperty(position = 7, notes = "string", value = "종료시간", example = "16:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime endTime;

    @ApiModelProperty(position = 8, required = true, value = "일정 대상자의 식별자")
    private List<TeamMemberFindResponseDto> targets = new ArrayList<>();

    @ApiModelProperty(position = 9, required = true, value = "외박 일정 여부", example = "false")
    private Boolean isSleepover;

    @Builder
    public TeamCalendarDefaultResponseDto(TeamCalendar teamCalendar, List<TeamMemberFindResponseDto> targets) {
        this.teamCalendarId = teamCalendar.getId();
        this.title = teamCalendar.getTitle();
        this.isSleepover = teamCalendar.getIsSleepover();
        if (teamCalendar.getContent() != null)
            this.content = teamCalendar.getContent();
        if (teamCalendar.getStartDate() != null)
            this.startDate = teamCalendar.getStartDate();
        if (teamCalendar.getEndDate() != null)
            this.endDate = teamCalendar.getEndDate();
        if (teamCalendar.getStartTime() != null)
            this.startTime = teamCalendar.getStartTime();
        if (teamCalendar.getEndTime() != null)
            this.endTime = teamCalendar.getEndTime();

        if (targets != null)
            this.targets = targets.stream().collect(Collectors.toList());
    }
}
