package idorm.idormServer.calendar.application.port.in.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.YearMonth;
import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({OfficialCalendarsFindRequest.class,
        ValidationSequence.NotNull.class
})
@Schema(title = "일정 조회 요청")
public class OfficialCalendarsFindRequest {

    @Schema(format = "string", description = "조회할 년/월", example = "2023-04")
    @NotNull(message = "년/월를 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    private YearMonth yearMonth;
}
