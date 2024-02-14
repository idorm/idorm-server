package idorm.idormServer.calendar.application.port.in.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import idorm.idormServer.calendar.domain.TeamCalendar;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(title = "팀 일정 기본 응답")
public class RoomMateCalendarResponse {

    @Schema(required = true, description = "팀일정 식별자", example = "1")
    private Long teamCalendarId;

    @Schema(required = true, description = "내용", example = "청소")
    private String title;

    @Schema(description = "내용", example = "방 청소만 하는게 아니라 화장실거울까지!")
    private String content;

    @Schema(format = "string", description = "시작일자", example = "2023-04-27")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Schema(format = "string", description = "종료일자", example = "2023-04-28")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Schema(format = "string", description = "시작시간", example = "15:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime startTime;

    @Schema(format = "string", description = "종료시간", example = "16:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime endTime;

    @Schema(required = true, description = "일정 대상자의 식별자")
    private List<RoomMateResponse> targets = new ArrayList<>();

    @Schema(required = true, description = "외박 일정 여부", example = "false")
    private Boolean isSleepover;

    @Builder
    public RoomMateCalendarResponse(TeamCalendar teamCalendar, List<RoomMateResponse> targets) {
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
            this.targets = new ArrayList<>(targets);
    }
}
