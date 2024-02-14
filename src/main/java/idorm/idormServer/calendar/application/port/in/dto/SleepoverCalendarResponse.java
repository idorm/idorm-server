package idorm.idormServer.calendar.application.port.in.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import idorm.idormServer.calendar.domain.TeamCalendar;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(title = "외박 일정 요약 응답")
public class SleepoverCalendarResponse {

    @Schema(required = true, description = "외박 일정 식별자", example = "1")
    private Long teamCalendarId;

    @Schema(name = "startDate", format = "string", description = "시작일자", example = "2023-04-27")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("startDate")
    private LocalDate startDate;

    @Schema(name = "endDate", format = "string", description = "종료일자", example = "2023-04-27")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("endDate")
    private LocalDate endDate;

    @Builder
    public SleepoverCalendarResponse(TeamCalendar teamCalendar) {
        this.teamCalendarId = teamCalendar.getId();

        if (teamCalendar.getStartDate() != null)
            this.startDate = teamCalendar.getStartDate();
        if (teamCalendar.getEndDate() != null)
            this.endDate = teamCalendar.getEndDate();
    }
}
