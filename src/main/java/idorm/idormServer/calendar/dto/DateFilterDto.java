package idorm.idormServer.calendar.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DateFilterDto {
    @ApiModelProperty(
            dataType = "string",
            example = "2022-09-01"
    )
    LocalDate startDate;
    @ApiModelProperty(
            dataType = "string",
            example = "2022-09-30"
    )
    LocalDate endDate;

    public LocalDateTime getStartDateTime() {
        if (startDate == null) return null;

        return startDate.atStartOfDay();
    }

    public LocalDateTime getEndDateTime() {
        if (endDate == null) return null;

        return endDate.plusDays(1).atStartOfDay();
    }
}
