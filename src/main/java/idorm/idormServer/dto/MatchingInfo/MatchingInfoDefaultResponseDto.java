package idorm.idormServer.dto.MatchingInfo;

import idorm.idormServer.domain.Dormitory;
import idorm.idormServer.domain.Gender;
import idorm.idormServer.domain.JoinPeriod;
import idorm.idormServer.domain.MatchingInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "온보딩 정보 응답")
public class MatchingInfoDefaultResponseDto {

    @ApiModelProperty(position = 1, required = true, value="id")
    private Long id;

    @ApiModelProperty(position = 2, required = true, dataType = "String", value = "{\"기숙사1\", \"기숙사2\", \"기숙사3\"}")
    private Dormitory dormNum;

    @ApiModelProperty(position = 3, required = true, dataType = "String", value = "{\"WEEK16\", \"WEEK24\"}")
    private JoinPeriod joinPeriod;

    @ApiModelProperty(position = 4, required = true, dataType = "String", value = "{\"FEMALE\", \"MALE\"}")
    private Gender gender;

    @ApiModelProperty(position = 5, required = true, dataType = "Integer", value = "나이")
    private Integer age;

    @ApiModelProperty(position = 6, required = true, dataType = "Boolean", value = "코골이 여부")
    private Boolean isSnoring;

    @ApiModelProperty(position = 7, required = true, dataType = "Boolean", value = "이갈이 여부")
    private Boolean isGrinding;

    @ApiModelProperty(position = 8, required = true, dataType = "Boolean", value = "흡연 여부")
    private Boolean isSmoking;

    @ApiModelProperty(position = 9, required = true, dataType = "Boolean", value = "실내 음식 여부")
    private Boolean isAllowedFood;

    @ApiModelProperty(position = 10, required = true, dataType = "Boolean", value = "이어폰 착용 여부")
    private Boolean isWearEarphones;

    @ApiModelProperty(position = 11, required = true, dataType = "String", value = "기상시간")
    private String wakeUpTime;

    @ApiModelProperty(position = 12, required = true, dataType = "String", value = "정리정돈")
    private String cleanUpStatus;

    @ApiModelProperty(position = 13, required = true, dataType = "String", value = "샤워시간")
    private String showerTime;

    @ApiModelProperty(position = 14, required = false, dataType = "String", value = "오픈채팅 링크")
    private String openKakaoLink;

    @ApiModelProperty(position = 15, required = false, dataType = "String")
    private String mbti;

    @ApiModelProperty(position = 16, required = false, value = "룸메에게 보내는 한마디")
    private String wishText;

    public MatchingInfoDefaultResponseDto(MatchingInfo matchingInfo) {
        this.id = matchingInfo.getId();
        this.dormNum = matchingInfo.getDormNum();
        this.joinPeriod = matchingInfo.getJoinPeriod();
        this.gender = matchingInfo.getGender();
        this.age = matchingInfo.getAge();
        this.isSnoring = matchingInfo.getIsSnoring();
        this.isGrinding = matchingInfo.getIsGrinding();
        this.isSmoking = matchingInfo.getIsSmoking();
        this.isAllowedFood = matchingInfo.getIsAllowedFood();
        this.isWearEarphones = matchingInfo.getIsWearEarphones();
        this.wakeUpTime = matchingInfo.getWakeUpTime();
        this.cleanUpStatus = matchingInfo.getCleanUpStatus();
        this.showerTime = matchingInfo.getShowerTime();
        this.openKakaoLink = matchingInfo.getOpenKakaoLink();
        this.mbti = matchingInfo.getMbti();
        this.wishText = matchingInfo.getWishText();
    }

}
