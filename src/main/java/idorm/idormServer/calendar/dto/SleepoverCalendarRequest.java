package idorm.idormServer.calendar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import idorm.idormServer.calendar.domain.RoomMateTeam;
import idorm.idormServer.calendar.domain.RoomMateTeamCalendar;
import io.swagger.v3.oas.annotations.media.Schema;
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
@GroupSequence({SleepoverCalendarRequest.class,
})
@Schema(title = "외박일정 기본 요청")
public class SleepoverCalendarRequest {

    @Schema(name = "startDate", format = "string", description = "시작일자", example = "2023-04-27")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("startDate")
    private LocalDate startDate;

    @Schema(name = "endDate", format = "string", description = "종료일자", example = "2023-04-27")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("endDate")
    private LocalDate endDate;

    public RoomMateTeamCalendar toEntity(RoomMateTeam team, Long target) {
        return RoomMateTeamCalendar.builder()
                .team(team)
                .targets(new ArrayList<>(Arrays.asList(target)))
                .isSleepover(true)
                .title("외박")
                .startDate(this.startDate)
                .endDate(this.endDate)
                .build();
    }
}