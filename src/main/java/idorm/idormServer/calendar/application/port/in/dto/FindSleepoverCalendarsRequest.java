package idorm.idormServer.calendar.application.port.in.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.YearMonth;
import javax.validation.constraints.NotNull;

public record FindSleepoverCalendarsRequest(
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    YearMonth yearMonth
) {

}
