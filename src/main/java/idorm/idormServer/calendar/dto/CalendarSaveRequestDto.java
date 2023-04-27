package idorm.idormServer.calendar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import idorm.idormServer.calendar.domain.Calendar;
import idorm.idormServer.common.ValidationSequence;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({CalendarSaveRequestDto.class,
        ValidationSequence.NotBlank.class,
        ValidationSequence.NotNull.class,
        ValidationSequence.Size.class,
})
@ApiModel(value = "일정 저장 요청")
public class CalendarSaveRequestDto {

    @ApiModelProperty(position = 1, required = true, value = "1기숙사 대상 여부", allowableValues = "true, false",
    example = "true")
    @NotNull(message = "1기숙사 대상 여부를 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private Boolean isDorm1Yn;

    @ApiModelProperty(position = 2, required = true, value = "2기숙사 대상 여부", allowableValues = "true, false",
            example = "true")
    @NotNull(message = "2기숙사 대상 여부를 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private Boolean isDorm2Yn;

    @ApiModelProperty(position = 3, required = true, value = "3기숙사 대상 여부", allowableValues = "true, false",
            example = "true")
    @NotNull(message = "3기숙사 대상 여부를 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private Boolean isDorm3Yn;

    @ApiModelProperty(position = 4, notes = "string", value = "시작일자", example = "2023-04-27")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty(position = 5, notes = "string", value = "종료일자", example = "2023-04-28")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ApiModelProperty(position = 6, notes = "string", value = "시작시간", example = "15:43:01")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime startTime;
    @ApiModelProperty(position = 7, required = true, example = "내용", value = "기숙사 화재 훈련")
    @NotBlank(message = "내용을 입력해 주세요.", groups = ValidationSequence.NotBlank.class)
    private String content;

    @ApiModelProperty(position = 8, example = "장소", value = "3기숙사 1층")
    @Size(max = 50, message = "장소는 ~50자 이내로 입력해 주세요.", groups = ValidationSequence.Size.class)
    private String location;

    @ApiModelProperty(position = 9, example = "참고용 웹 링크",
            value = "https://www.inu.ac.kr/user/indexMain.do?command=&siteId=dorm")
    @Size(max = 300, message = "링크는 ~300자 이내로 입력해 주세요.", groups = ValidationSequence.Size.class)
    private String url;

    public Calendar toEntity() {
        return Calendar.builder()
                .isDorm1Yn(this.isDorm1Yn)
                .isDorm2Yn(this.isDorm2Yn)
                .isDorm3Yn(this.isDorm3Yn)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .startTime(this.startTime)
                .content(this.content)
                .location(this.location)
                .url(this.url)
                .build();
    }
}
