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
import java.time.LocalDate;

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

    @Schema(required = true, description = "제목", example = "기숙사 화재 훈련")
    @NotBlank(message = "제목을 입력해 주세요.", groups = ValidationSequence.NotBlank.class)
    private String title;
}

