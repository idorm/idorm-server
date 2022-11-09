package idorm.idormServer.matchingInfo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "MatchingInfo 수정 요청")
public class MatchingInfoUpdateRequestDto {

    @NotNull(message = "기숙사 입력은 필수입니다.")
    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "기숙사 분류", example = "DORM1")
    private String dormNum;

    @NotNull(message = "입사기간 입력은 필수입니다.")
    @ApiModelProperty(position = 2, required = true, dataType = "String", value = "입사기간", example = "WEEK16")
    private String joinPeriod;

    @NotNull(message = "성별 입력은 필수입니다.")
    @ApiModelProperty(position = 3, required = true, dataType = "String", value = "성별", example = "FEMALE")
    private String gender;

    @ApiModelProperty(position = 4, required = true, dataType = "Integer", example = "20")
    private Integer age;

    @ApiModelProperty(position = 5, required = true, dataType = "Boolean", example = "true")
    private Boolean isSnoring;

    @ApiModelProperty(position = 6, required = true, dataType = "Boolean", example = "true")
    private Boolean isGrinding;

    @ApiModelProperty(position = 7, required = true, dataType = "Boolean", example = "true")
    private Boolean isSmoking;

    @ApiModelProperty(position = 8, required = true, dataType = "Boolean", example = "true")
    private Boolean isAllowedFood;

    @ApiModelProperty(position = 9, required = true, dataType = "Boolean", example = "true")
    private Boolean isWearEarphones;

    @ApiModelProperty(position = 10, required = true, dataType = "String", example = "오전 9시에 기상합니다.")
    private String wakeUpTime;

    @NotNull(message = "정리정돈 상태 입력은 필수입니다.")
    @ApiModelProperty(position = 11, required = true, dataType = "String", example = "밤 10시에 늘 청소해요.")
    private String cleanUpStatus;

    @NotNull(message = "샤워 시간 입력은 필수입니다.")
    @ApiModelProperty(position = 12, required = true, dataType = "String", example = "아침 8시, 밤 12시에 주로 씻어요.")
    private String showerTime;

    @NotBlank(message = "오픈 채팅 링크를 입력해 주세요.")
    @ApiModelProperty(position = 13, dataType = "String", example = "https://open.kakao.com/o/szaIN6ze")
    private String openKakaoLink;

    @ApiModelProperty(position = 14, dataType = "String", example = "ISTP")
    private String mbti;

    @ApiModelProperty(position = 15, dataType = "String", example = "적당한 선을 지키면서 친해질 수 있는 룸메이트 구해요 :)")
    private String wishText;

}
