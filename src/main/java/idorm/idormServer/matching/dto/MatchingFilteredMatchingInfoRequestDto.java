package idorm.idormServer.matching.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel(value = "Matching 매칭정보 필터링 요청")
public class MatchingFilteredMatchingInfoRequestDto {

    @NotNull(message = "기숙사 입력은 필수입니다.")
    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "DORM1, DORM2, DORM3",
            example = "DORM1")
    private String dormNum;

    @NotNull(message = "입사기간 입력은 필수입니다.")
    @ApiModelProperty(position = 2, required = true, dataType = "String", value = "WEEK16, WEEK24", example = "WEEK16")
    private String joinPeriod;

    @NotNull(message = "매칭을 원하는 룸메이트의 최소 나이 입력은 필수입니다.")
    @Range(min = 20, max = 100, message = "나이는 20과 100 사이여야 합니다.")
    @ApiModelProperty(position = 3, required = true, dataType = "Integer", value = "20", example = "20")
    private Integer minAge;

    @NotNull(message = "매칭을 원하는 룸메이트의 최대 나이 입력은 필수입니다.")
    @Range(min = 20, max = 100, message = "나이는 20과 100 사이여야 합니다.")
    @ApiModelProperty(position = 4, required = true, dataType = "Integer", value = "30", example = "30")
    private Integer maxAge;

    @NotNull(message = "코골이 여부 입력은 필수입니다.")
    @ApiModelProperty(position = 5, required = true, dataType = "Boolean", value = "코골이 여부", example = "false")
    private Boolean isSnoring;

    @NotNull(message = "흡연 여부 입력은 필수입니다.")
    @ApiModelProperty(position = 6, required = true, dataType = "Boolean", value = "흡연 여부", example = "false")
    private Boolean isSmoking;

    @NotNull(message = "이갈이 여부 입력은 필수입니다.")
    @ApiModelProperty(position = 7, required = true, dataType = "Boolean", value = "이갈이 여부", example = "false")
    private Boolean isGrinding;

    @NotNull(message = "이어폰 착용 의사 여부 입력은 필수입니다.")
    @ApiModelProperty(position = 8, required = true, dataType = "Boolean", value = "이어폰 착용 의사 여부", example = "true")
    private Boolean isWearEarphones;

    @NotNull(message = "살외 음식 섭취 허용 여부 입력은 필수입니다.")
    @ApiModelProperty(position = 9, required = true, dataType = "Boolean", value = "실내 음식 섭취 허용 여부",
            example = "true")
    private Boolean isAllowedFood;
}
