package idorm.idormServer.matching.dto;

import idorm.idormServer.common.ValidationSequence;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({MatchingFilteredMatchingInfoRequestDto.class,
        ValidationSequence.NotBlank.class,
        ValidationSequence.NotNull.class,
        ValidationSequence.Range.class,
})
@ApiModel(value = "Matching 매칭정보 필터링 요청")
public class MatchingFilteredMatchingInfoRequestDto {

    @ApiModelProperty(position = 1, required = true, value = "기숙사 분류", allowableValues = "DORM1, DORM2, DORM3",
            example = "DORM1")
    @NotBlank(message = "기숙사 분류를 입력해 주세요.", groups = ValidationSequence.NotBlank.class)
    private String dormCategory;

    @ApiModelProperty(position = 2, required = true, value = "입사기간", allowableValues = "WEEK16, WEEK24",
            example = "WEEK16")
    @NotBlank(message = "입사기간을 입력해 주세요.", groups = ValidationSequence.NotBlank.class)
    private String joinPeriod;

    @ApiModelProperty(position = 3, required = true, value = "필터링 최소 나이", example = "20")
    @NotNull(message = "매칭을 원하는 룸메이트의 최소 나이 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    @Range(min = 20, max = 100, message = "나이는 20~100살 이내여야 합니다.", groups = ValidationSequence.Range.class)
    private Integer minAge;

    @ApiModelProperty(position = 4, required = true, value = "필터링 최대 나이", example = "30")
    @NotNull(message = "매칭을 원하는 룸메이트의 최대 나이 입력은 필수입니다.")
    @Range(min = 20, max = 100, message = "나이는 20~100살 이내여야 합니다.", groups = ValidationSequence.Range.class)
    private Integer maxAge;

    @ApiModelProperty(position = 5, required = true, example = "true", value = "코골이 여부")
    @NotNull(message = "코골이 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private Boolean isSnoring;

    @ApiModelProperty(position = 6, required = true, example = "true", value = "이갈이 여부")
    @NotNull(message = "이갈이 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private Boolean isGrinding;

    @ApiModelProperty(position = 7, required = true, example = "true", value = "흡연 여부")
    @NotNull(message = "흡연 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private Boolean isSmoking;

    @ApiModelProperty(position = 8, required = true, example = "true", value = "실내 음식 섭취 여부")
    @NotNull(message = "실내 음식 허용 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private Boolean isAllowedFood;

    @ApiModelProperty(position = 9, required = true, example = "true", value = "이어폰 착용 여부")
    @NotNull(message = "이어폰 착용 의사 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private Boolean isWearEarphones;
}
