package idorm.idormServer.calendar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import idorm.idormServer.common.ValidationSequence;
import io.swagger.v3.oas.annotations.media.Schema;
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
@GroupSequence({OfficialCalendarUpdateRequest.class,
        ValidationSequence.NotBlank.class,
        ValidationSequence.NotNull.class,
        ValidationSequence.Size.class,
})
@Schema(title = "공식 일정 수정 요청")
public class OfficialCalendarUpdateRequest {

    @Schema(description = "공식 일정 식별자", example = "1")
    @NotNull(message = "공식 일정 식별자를 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private Long calendarId;

    @Schema(description = "생활원 공식일정 게시글 번호", example = "731406")
    @Size(message = "게시글 번호는 4~10자 이내여야 합니다.", groups = ValidationSequence.NotNull.class)
    private String inuPostId;

    @Schema(required = true, description = "1기숙사 대상 여부", allowableValues = "true, false",
            example = "true")
    @NotNull(message = "1기숙사 대상 여부를 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private Boolean isDorm1Yn;

    @Schema(required = true, description = "2기숙사 대상 여부", allowableValues = "true, false",
            example = "true")
    @NotNull(message = "2기숙사 대상 여부를 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private Boolean isDorm2Yn;

    @Schema(required = true, description = "3기숙사 대상 여부", allowableValues = "true, false",
            example = "true")
    @NotNull(message = "3기숙사 대상 여부를 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private Boolean isDorm3Yn;

    @Schema(format = "string", description = "시작일자", example = "2023-04-27")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Schema(format = "string", description = "종료일자", example = "2023-04-28")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Schema(format = "string", description = "시작시간", example = "15:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime startTime;

    @Schema(format = "string", description = "종료시간", example = "16:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime endTime;

    @Schema(required = true, description = "제목", example = "기숙사 화재 훈련")
    @NotBlank(message = "제목을 입력해 주세요.", groups = ValidationSequence.NotBlank.class)
    private String title;

    @Schema(required = true, description = "내용", example = "기숙사 화재 훈련")
    @NotBlank(message = "내용을 입력해 주세요.", groups = ValidationSequence.NotBlank.class)
    private String content;

    @Schema(description = "장소", example = "3기숙사 1층")
    @Size(max = 50, message = "장소는 ~50자 이내여야 합니다.", groups = ValidationSequence.Size.class)
    private String location;

    @Schema(description = "참고용 웹 링크",
            example = "https://www.inu.ac.kr/user/indexMain.do?command=&siteId=dorm")
    @Size(max = 300, message = "링크는 ~300자 이내여야 합니다.", groups = ValidationSequence.Size.class)
    private String url;
}

