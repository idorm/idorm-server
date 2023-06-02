package idorm.idormServer.calendar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import idorm.idormServer.calendar.domain.Calendar;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel(value = "Calendar 관리자용 응답")
public class CalendarAdminResponseDto {

    @ApiModelProperty(position = 1, required = true, value= "캘린더 식별자")
    private Long calendarId;
    @ApiModelProperty(position = 2, required = true, value= "1기숙사 대상 여부")
    private Boolean isDorm1Yn;
    @ApiModelProperty(position = 3, required = true, value= "2기숙사 대상 여부")
    private Boolean isDorm2Yn;
    @ApiModelProperty(position = 4, required = true, value= "3기숙사 대상 여부")
    private Boolean isDorm3Yn;
    @ApiModelProperty(position = 5, value= "시작 일자")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @ApiModelProperty(position = 6, value= "종료 일자")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ApiModelProperty(position = 7, value= "시작 시간")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime startTime;

    @ApiModelProperty(position = 8, value= "종료 시간")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime endTime;

    @ApiModelProperty(position = 9, value= "내용")
    private String content;

    @ApiModelProperty(position = 10, value= "장소")
    private String location;

    @ApiModelProperty(position = 11, value= "참고 링크")
    private String url;

    public CalendarAdminResponseDto(Calendar calendar) {

        this.calendarId = calendar.getId();
        this.isDorm1Yn = calendar.getIsDorm1Yn();
        this.isDorm2Yn = calendar.getIsDorm2Yn();
        this.isDorm3Yn = calendar.getIsDorm3Yn();

        if (calendar.getStartDate() != null)
            this.startDate = calendar.getStartDate();

        if (calendar.getEndDate() != null)
            this.endDate = calendar.getEndDate();

        if (calendar.getStartTime() != null)
            this.startTime = calendar.getStartTime();
        if (calendar.getEndTime() != null)
            this.endTime = calendar.getEndTime();

        this.content = calendar.getContent();

        if (calendar.getLocation() != null)
            this.location = calendar.getLocation();

        if (calendar.getUrl() != null)
            this.url = calendar.getUrl();
    }
}
