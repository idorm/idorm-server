package idorm.idormServer.calendar.dto;

import idorm.idormServer.calendar.domain.OfficialCalendar;
import idorm.idormServer.photo.domain.OfficialCalendarPhoto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(title = "Calendar 기본 응답")
public class OfficialCalendarResponse {

    @Schema(required = true, description= "캘린더 식별자")
    private Long calendarId;
    @Schema(required = true, description= "1기숙사 대상 여부")
    private Boolean isDorm1Yn;
    @Schema(required = true, description= "2기숙사 대상 여부")
    private Boolean isDorm2Yn;
    @Schema(required = true, description= "3기숙사 대상 여부")
    private Boolean isDorm3Yn;
    @Schema(description= "시작 일자")
    private LocalDate startDate;
    @Schema(description= "종료 일자")
    private LocalDate endDate;
    @Schema(description= "시작 시간")
    private LocalTime startTime;
    @Schema(description= "종료 시간")
    private LocalTime endTime;
    @Schema(description= "내용")
    private String content;
    @Schema(description= "장소")
    private String location;
    @Schema(description= "참고 링크")
    private String url;
    @Schema(description = "참고 사진")
    private List<OfficialCalendarPhoto> calendarPhotos = new ArrayList<>();

    public OfficialCalendarResponse(OfficialCalendar calendar) {

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

        if (calendar.getWebsiteUrl() != null)
            this.url = calendar.getWebsiteUrl();

        if (calendar.getOfficialCalendarPhotos().size() > 0)
            this.calendarPhotos = new ArrayList<>(calendar.getOfficialCalendarPhotos());
    }
}
