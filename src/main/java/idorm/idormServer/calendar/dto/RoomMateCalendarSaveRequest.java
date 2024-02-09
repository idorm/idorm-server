package idorm.idormServer.calendar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.calendar.domain.TeamCalendar;
import idorm.idormServer.common.ValidationSequence;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({RoomMateCalendarSaveRequest.class,
        ValidationSequence.NotBlank.class,
        ValidationSequence.NotNull.class,
        ValidationSequence.Size.class
})
@Schema(title = "팀 일정 저장 요청")
public class RoomMateCalendarSaveRequest {

    @Schema(required = true, description = "내용", example = "청소")
    @NotBlank(message = "내용을 입력해 주세요.", groups = ValidationSequence.NotBlank.class)
    @Size(max = 15, message = "제목은 ~15자 이내여야 합니다.", groups = ValidationSequence.Size.class)
    private String title;

    @Schema(description = "내용", example = "방 청소만 하는게 아니라 화장실거울까지!")
    @Size(max = 100, message = "내용은 ~100자 이내여야 합니다.", groups = ValidationSequence.Size.class)
    private String content;

    @Schema(name = "startDate", format = "string", description = "시작일자", example = "2023-04-27")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("startDate")
    private LocalDate startDate;

    @Schema(name = "endDate", format = "string", description = "종료일자", example = "2023-04-27")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("endDate")
    private LocalDate endDate;

    @Schema(name = "startTime", format = "string", description = "시작시간", example = "15:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @JsonProperty("startTime")
    private LocalTime startTime;

    @Schema(name = "endTime", format = "string", description = "종료시간", example = "15:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @JsonProperty("endTime")
    private LocalTime endTime;

    @Schema(name = "targets", description = "일정 대상자의 식별자")
    private List<Long> targets = new ArrayList<>();

    public TeamCalendar toEntity(Team team) {
        return TeamCalendar.builder()
                .team(team)
                .targets(this.targets)
                .isSleepover(false)
                .title(title)
                .content(content)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .build();
    }
}
