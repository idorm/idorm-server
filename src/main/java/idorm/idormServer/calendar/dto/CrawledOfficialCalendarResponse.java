package idorm.idormServer.calendar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import idorm.idormServer.calendar.domain.OfficialCalendar;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(title = "[관리자 용도] 크롤링 공식 일정 응답")
public class CrawledOfficialCalendarResponse {

    @Schema(required = true, description = "아이돔 공식 일정 식별자")
    private Long officialCalendarId;

    @Schema(required = true, description= "생활원 홈페이지 게시글 식별자")
    private String inuPostId;

    @Schema(required = true, description= "생활원 홈페이지 게시글 제목")
    private String title;

    @Schema(required = true, description= "생활원 홈페이지 게시글 작성일")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate inuPostCreatedAt;

    @Schema(required = true, description= "생활원 홈페이지 게시글 링크")
    private String postUrl;

    @Schema(required = true, description= "사용자 공개 여부(관리자 허가 여부)")
    private Boolean isPublic;

    public CrawledOfficialCalendarResponse(OfficialCalendar officialCalendar) {
        this.officialCalendarId = officialCalendar.getId();
        this.inuPostId = officialCalendar.getInuPostId();
        this.title = officialCalendar.getTitle();
        this.inuPostCreatedAt = officialCalendar.getInuPostCreatedAt();
        this.postUrl = officialCalendar.getPostUrl();
        this.isPublic = officialCalendar.getIsPublic();
    }
}
