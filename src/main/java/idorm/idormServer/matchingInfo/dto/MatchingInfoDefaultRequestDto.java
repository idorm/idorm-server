package idorm.idormServer.matchingInfo.dto;

import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.member.domain.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@ApiModel(value = "MatchingInfo 저장 요청")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingInfoDefaultRequestDto {

    @NotBlank(message = "기숙사 분류를 입력해 주세요.")
    @ApiModelProperty(position = 1, required = true, dataType = "String", value = "기숙사 분류", example = "DORM1")
    private String dormNum;

    @NotBlank(message = "입사기간을 입력해 주세요.")
    @ApiModelProperty(position = 2, required = true, dataType = "String", value = "입사기간", example = "WEEK16")
    private String joinPeriod;

    @NotBlank(message = "성별을 입력해 주세요.")
    @ApiModelProperty(position = 3, required = true, dataType = "String", value = "성별", example = "FEMALE")
    private String gender;

    @NotNull(message = "나이 입력은 필수입니다.")
    @ApiModelProperty(position = 4, required = true, dataType = "Integer", value = "나이", example = "20")
    private Integer age;

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
    @ApiModelProperty(position = 8, required = true, dataType = "Boolean", value = "이어폰 착용 의사 여부", example = "false")
    private Boolean isWearEarphones;

    @NotNull(message = "실내 음식 허용 여부 입력은 필수입니다.")
    @ApiModelProperty(position = 9, required = true, dataType = "Boolean", value = "실내 음식 섭취 허용 여부", example = "false")
    private Boolean isAllowedFood;

    @NotBlank(message = "기상시간을 입력해 주세요.")
    @ApiModelProperty(position = 10, required = true, dataType = "String", value = "기상 시간", example = "주로 아침 8시에 일어납니다.")
    private String wakeupTime;

    @NotBlank(message = "청소 상태를 입력해 주세요.")
    @ApiModelProperty(position = 11, required = true, dataType = "String", value = "청소 상태", example = "적당히 깨끗한 걸 좋아해요.")
    private String cleanUpStatus;

    @NotBlank(message = "샤워 시간을 입력해 주세요.")
    @ApiModelProperty(position = 12, required = true, dataType = "String", value = "샤워 시간", example = "주로 아침 8시 30분, 밤 11시에 씻어요.")
    private String showerTime;

    @ApiModelProperty(position = 13, required = false, dataType = "String", value = "mbti", example = "ISTP")
    private String mbti;

    @ApiModelProperty(position = 14, required = false, dataType = "String", value = "룸메에게 하고싶은 말", example = "주말에는 주로 본가에 갑니다. 함께 잘 지내봐요!")
    private String wishText;

    @NotBlank(message = "오픈 채팅 링크를 입력해 주세요.")
    @ApiModelProperty(position = 15, required = true, dataType = "String", value = "오픈채팅 링크", example = "https://open.kakao.com/o/szaIN6ze")
    private String openKakaoLink;

    public MatchingInfoDefaultRequestDto(MatchingInfo matchingInfo) {

        this.dormNum = matchingInfo.getDormNum();
        this.joinPeriod = matchingInfo.getJoinPeriod();
        this.gender = matchingInfo.getGender();
        this.age = matchingInfo.getAge();
        this.isSnoring = matchingInfo.getIsSnoring();
        this.isSmoking = matchingInfo.getIsSmoking();
        this.isGrinding = matchingInfo.getIsGrinding();
        this.isWearEarphones = matchingInfo.getIsWearEarphones();
        this.isAllowedFood = matchingInfo.getIsAllowedFood();
        this.wakeupTime = matchingInfo.getWakeUpTime();
        this.cleanUpStatus = matchingInfo.getCleanUpStatus();
        this.showerTime = matchingInfo.getShowerTime();
        this.mbti = matchingInfo.getMbti();
        this.wishText = matchingInfo.getWishText();
        this.openKakaoLink = matchingInfo.getOpenKakaoLink();
    }

    public MatchingInfo toEntity(Member member) {
        return MatchingInfo.builder()
                .dormNum(dormNum)
                .joinPeriod(joinPeriod)
                .gender(gender)
                .age(age)
                .isSnoring(isSnoring)
                .isSmoking(isSmoking)
                .isAllowedFood(isAllowedFood)
                .isWearEarphones(isWearEarphones)
                .isGrinding(isGrinding)
                .wakeUpTime(wakeupTime)
                .cleanUpStatus(cleanUpStatus)
                .showerTime(showerTime)
                .openKakaoLink(openKakaoLink)
                .mbti(mbti)
                .wishText(wishText)
                .member(member)
                .build();
    }
}