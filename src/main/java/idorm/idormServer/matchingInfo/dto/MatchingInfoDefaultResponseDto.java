package idorm.idormServer.matchingInfo.dto;

import idorm.idormServer.matchingInfo.domain.Dormitory;
import idorm.idormServer.matchingInfo.domain.Gender;
import idorm.idormServer.matchingInfo.domain.JoinPeriod;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "온보딩 정보 응답")
public class MatchingInfoDefaultResponseDto {

    @ApiModelProperty(position = 1, value="식별자")
    private Long id;

    @ApiModelProperty(position = 2, example = "기숙사1, 기숙사2, 기숙사3")
    private Dormitory dormNum;

    @ApiModelProperty(position = 3, example = "WEEK16, WEEK24")
    private JoinPeriod joinPeriod;

    @ApiModelProperty(position = 4, example = "FEMALE, MALE")
    private Gender gender;

    @ApiModelProperty(position = 5, value = "나이")
    private Integer age;

    @ApiModelProperty(position = 6, value = "코골이 여부")
    private Boolean isSnoring;

    @ApiModelProperty(position = 7, value = "이갈이 여부")
    private Boolean isGrinding;

    @ApiModelProperty(position = 8, value = "흡연 여부")
    private Boolean isSmoking;

    @ApiModelProperty(position = 9, value = "실내 음식 허용 여부")
    private Boolean isAllowedFood;

    @ApiModelProperty(position = 10, value = "이어폰 착용 여부")
    private Boolean isWearEarphones;

    @ApiModelProperty(position = 11, value = "기상 시간")
    private String wakeUpTime;

    @ApiModelProperty(position = 12, value = "정리 정돈 상태")
    private String cleanUpStatus;

    @ApiModelProperty(position = 13, value = "샤워 시간")
    private String showerTime;

    @ApiModelProperty(position = 14, value = "오픈 채팅 링크")
    private String openKakaoLink;

    @ApiModelProperty(position = 15, value = "ISTP")
    private String mbti;

    @ApiModelProperty(position = 16, value = "룸메에게 하고싶은 말")
    private String wishText;

    @ApiModelProperty(position = 17, value = "매칭이미지 공개 여부")
    private Boolean isMatchingInfoPublic;

    @ApiModelProperty(position = 18, value = "aaa@inu.ac.kr")
    private String memberEmail;


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
        this.isMatchingInfoPublic = matchingInfo.getIsMatchingInfoPublic();
        this.memberEmail = matchingInfo.getMember().getEmail();
    }

}
