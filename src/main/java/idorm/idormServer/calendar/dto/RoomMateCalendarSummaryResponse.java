package idorm.idormServer.calendar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import idorm.idormServer.calendar.domain.RoomMateTeamCalendar;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title = "팀 일정 요약 응답")
public class RoomMateCalendarSummaryResponse {

    @Schema(required = true, description = "팀일정 식별자", example = "1")
    @JsonProperty("teamCalendarId")
    private Long teamCalendarId;

    @Schema(required = true, description = "내용", example = "청소")
    @JsonProperty("title")
    private String title;

    @Schema(name = "startDate", format = "string", description = "시작일자", example = "2023-04-27")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("startDate")
    private LocalDate startDate;

    @Schema(name = "endDate", format = "string", description = "종료일자", example = "2023-04-27")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("endDate")
    private LocalDate endDate;

    @Schema(name = "targets", description = "일정 대상자의 식별자")
    private List<RoomMateResponse> targets = new ArrayList<>();

    @Builder
    public RoomMateCalendarSummaryResponse(RoomMateTeamCalendar teamCalendar, List<RoomMateResponse> targets) {
        this.teamCalendarId = teamCalendar.getId();
        this.title = teamCalendar.getTitle();

        if (teamCalendar.getStartDate() != null)
            this.startDate = teamCalendar.getStartDate();
        if (teamCalendar.getEndDate() != null)
            this.endDate = teamCalendar.getEndDate();

        if (targets != null)
            this.targets = targets.stream().collect(Collectors.toList());
    }
}
