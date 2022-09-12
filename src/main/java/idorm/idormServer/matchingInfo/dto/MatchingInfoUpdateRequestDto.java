package idorm.idormServer.matchingInfo.dto;

import idorm.idormServer.matchingInfo.domain.Dormitory;
import idorm.idormServer.matchingInfo.domain.Gender;
import idorm.idormServer.matchingInfo.domain.JoinPeriod;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "온보딩정보 수정 요청")
public class MatchingInfoUpdateRequestDto {

    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "{\"기숙사1\", \"기숙사2\", \"기숙사3\"}", example = "기숙사1")
    private Dormitory dormNum;

    @ApiModelProperty(position = 2, required = true, dataType = "String", value = "{\"WEEK16\", \"WEEK24\"}", example = "WEEK16")
    private JoinPeriod joinPeriod;

    @ApiModelProperty(position = 3, required = true, dataType = "String", value = "{\"FEMALE\", \"MALE\"}", example = "FEMALE")
    private Gender gender;

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

    @ApiModelProperty(position = 11, required = true, dataType = "String", example = "밤 10시에 늘 청소해요.")
    private String cleanUpStatus;

    @ApiModelProperty(position = 12, required = true, dataType = "String", example = "아침 8시, 밤 12시에 주로 씻어요.")
    private String showerTime;

    @ApiModelProperty(position = 13, required = false, dataType = "String", example = "https://open.kakao.com/o/szaIN6ze")
    private String openKakaoLink;

    @ApiModelProperty(position = 14, required = false, dataType = "String", example = "ISTP")
    private String mbti;

    @ApiModelProperty(position = 15, required = false, dataType = "String", example = "적당한 선을 지키면서 친해질 수 있는 룸메이트 구해요 :)")
    private String wishText;

}
