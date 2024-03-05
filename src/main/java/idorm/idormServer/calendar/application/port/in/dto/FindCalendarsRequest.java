package idorm.idormServer.calendar.application.port.in.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.YearMonth;
import javax.validation.constraints.NotNull;

public record FindCalendarsRequest(
    @NotNull(message = "년/월를 입력해 주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    YearMonth yearMonth
) {

}
