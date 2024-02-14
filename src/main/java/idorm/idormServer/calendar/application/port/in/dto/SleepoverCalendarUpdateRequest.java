package idorm.idormServer.calendar.application.port.in.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import idorm.idormServer.common.ValidationSequence;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({
        SleepoverCalendarUpdateRequest.class,
        ValidationSequence.NotNull.class
})
@Schema(title = "외박일정 수정 요청")
public class SleepoverCalendarUpdateRequest {

    @Schema(name = "teamCalendarId", required = true, description= "팀 일정 식별자", example = "1")
    @NotNull(message = "내용을 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    @Positive(message = "수정할 팀 일정 식별자는 양수만 가능합니다.")
    @JsonProperty("teamCalendarId")
    private Long teamCalendarId;

    @Schema(name = "startDate", format = "string", description = "시작일자", example = "2023-04-27")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("startDate")
    private LocalDate startDate;

    @Schema(name = "endDate", format = "string", description = "종료일자", example = "2023-04-27")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("endDate")
    private LocalDate endDate;
}
