package idorm.idormServer.calendar.dto;

import idorm.idormServer.calendar.domain.OfficialCalendar;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(title = "[사용자 용도] 공식 일정 응답")
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

    @Schema(description= "제목")
    private String title;

    @Schema(description= "생활원 홈페이지 공지 게시글 링크")
    private String postUrl;

    @Schema(description= "생활원 홈페이지 게시글 작성일")
    private String inuPostCreatedAt;

    public OfficialCalendarResponse(OfficialCalendar calendar) {

        this.calendarId = calendar.getId();
        this.isDorm1Yn = calendar.getIsDorm1Yn();
        this.isDorm2Yn = calendar.getIsDorm2Yn();
        this.isDorm3Yn = calendar.getIsDorm3Yn();

        if (calendar.getStartDate() != null)
            this.startDate = calendar.getStartDate();

        if (calendar.getEndDate() != null)
            this.endDate = calendar.getEndDate();

        if (calendar.getPostUrl() != null)
            this.postUrl = calendar.getPostUrl();

        this.inuPostCreatedAt = calendar.getInuPostCreatedAt().toString();
    }
}
