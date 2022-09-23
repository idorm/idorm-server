package idorm.idormServer.calendar.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Getter
@Setter
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
}
