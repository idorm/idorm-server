package idorm.idormServer.matchingInfo.dto;

import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.matchingInfo.domain.JoinPeriod;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.matchingInfo.domain.Gender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "MatchingInfo 응답")
public class MatchingInfoDefaultResponseDto {

    @ApiModelProperty(position = 1, required = true, value= "매칭정보 식별자", example = "2")
    private Long matchingInfoId;

    @ApiModelProperty(position = 2, required = true, value = "기숙사 분류", allowableValues = "DORM1, DORM2, DORM3",
            example = "DORM1")
    private DormCategory dormCategory;

    @ApiModelProperty(position = 3, required = true, value = "입사기간", allowableValues = "WEEK16, WEEK24",
            example = "WEEK16")
    private JoinPeriod joinPeriod;

    @ApiModelProperty(position = 4, required = true, value = "성별", allowableValues = "MALE, FEMALE", example = "MALE")
    private Gender gender;

    @ApiModelProperty(position = 5, value = "나이", example = "20")
    private Integer age;

    @ApiModelProperty(position = 6, value = "코골이 여부", example = "false")
    private Boolean isSnoring;

    @ApiModelProperty(position = 7, value = "이갈이 여부", example = "false")
    private Boolean isGrinding;

    @ApiModelProperty(position = 8, value = "흡연 여부", example = "false")
    private Boolean isSmoking;

    @ApiModelProperty(position = 9, value = "실내 음식 허용 여부", example = "false")
    private Boolean isAllowedFood;

    @ApiModelProperty(position = 10, value = "이어폰 착용 여부", example = "false")
    private Boolean isWearEarphones;

    @ApiModelProperty(position = 11, value = "기상시간", example = "아침 7시 기상~~")
    private String wakeUpTime;

    @ApiModelProperty(position = 12, value = "정리정돈", example = "깨끗해요~~")
    private String cleanUpStatus;

    @ApiModelProperty(position = 13, value = "샤워시간", example = "7시에 씻어요~~")
    private String showerTime;

    @ApiModelProperty(position = 14, value = "오픈채팅 링크", example = "링크~~")
    private String openKakaoLink;

    @ApiModelProperty(position = 15, value = "mbti", example = "ESTJ")
    private String mbti;

    @ApiModelProperty(position = 16, value = "룸메에게 보내는 한마디", example = "혜원이랑 룸메 하고 싶어요 ㅎ_ㅎ")
    private String wishText;

    @ApiModelProperty(position = 17, value = "매칭이미지 공개 여부", example = "true")
    private Boolean isMatchingInfoPublic;

    @ApiModelProperty(position = 18, value = "회원 이메일", example = "aaa@inu.ac.kr")
    private String memberEmail;

    public MatchingInfoDefaultResponseDto(MatchingInfo matchingInfo) {
        this.matchingInfoId = matchingInfo.getId();
        this.dormCategory = DormCategory.valueOf(matchingInfo.getDormCategory());
        this.joinPeriod = JoinPeriod.valueOf(matchingInfo.getJoinPeriod());
        this.gender = Gender.valueOf(matchingInfo.getGender());
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
