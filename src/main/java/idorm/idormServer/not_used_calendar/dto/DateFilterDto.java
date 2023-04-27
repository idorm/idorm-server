//package idorm.idormServer.calendar.dto;
//
//import io.swagger.annotations.ApiModelProperty;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.springframework.format.annotation.DateTimeFormat;
//import springfox.documentation.annotations.ApiIgnore;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class DateFilterDto {
//    @ApiModelProperty(
//            dataType = "string",
//            example = "2022-09-01"
//    )
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    LocalDate startDate;
//    @ApiModelProperty(
//            dataType = "string",
//            example = "2022-09-30"
//    )
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    LocalDate endDate;
//
//    public LocalDateTime _getStartDateTime() {
//        if (startDate == null) return null;
//
//        return startDate.atStartOfDay();
//    }
//
//    public LocalDateTime _getEndDateTime() {
//        if (endDate == null) return null;
//
//        return endDate.plusDays(1).atStartOfDay();
//    }
//}
