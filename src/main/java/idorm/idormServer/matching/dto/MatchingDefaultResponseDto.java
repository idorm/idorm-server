package idorm.idormServer.matching.dto;

import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.matchingInfo.domain.JoinPeriod;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;

import idorm.idormServer.matchingInfo.domain.Gender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel(value = "Matching 응답")
public class MatchingDefaultResponseDto {

    @ApiModelProperty(position = 1, value="멤버 식별자", example = "2")
    private Long memberId;

    @ApiModelProperty(position = 2, value="매칭정보 식별자", example = "1")
    private Long matchingInfoId;

    @ApiModelProperty(position = 3, required = true, value = "기숙사 분류: DORM1, DORM2, DORM3", example = "DORM1")
    private DormCategory dormCategory;

    @ApiModelProperty(position = 4, required = true, value = "입사기간: WEEK16, WEEK24", example = "WEEK16")
    private JoinPeriod joinPeriod;

    @ApiModelProperty(position = 5, required = true, value = "성별: MALE, FEMALE", example = "MALE")
    private Gender gender;

    @ApiModelProperty(position = 6, value = "나이", example = "20")
    private Integer age;

    @ApiModelProperty(position = 7, value = "코골이 여부", example = "false")
    private Boolean isSnoring;

    @ApiModelProperty(position = 8, value = "이갈이 여부", example = "false")
    private Boolean isGrinding;

    @ApiModelProperty(position = 9, value = "흡연 여부", example = "false")
    private Boolean isSmoking;

    @ApiModelProperty(position = 10, value = "실내 음식 허용 여부", example = "false")
    private Boolean isAllowedFood;

    @ApiModelProperty(position = 11, value = "이어폰 착용 여부", example = "false")
    private Boolean isWearEarphones;

    @ApiModelProperty(position = 12, value = "기상 시간", example = "아침 7시 기상")
    private String wakeUpTime;

    @ApiModelProperty(position = 13, value = "정리 정돈 상태", example = "더러워용")
    private String cleanUpStatus;

    @ApiModelProperty(position = 14, value = "샤워 시간", example = "아침7시, 밤 12시에 주로 씻어요")
    private String showerTime;

    @ApiModelProperty(position = 15, value = "오픈 채팅 링크", example = "링크~")
    private String openKakaoLink;

    @ApiModelProperty(position = 16, value = "mbti", example = "ISTP")
    private String mbti;

    @ApiModelProperty(position = 17, value = "룸메에게 하고싶은 말", example = "즐거운 한 학기를 보내보아요~")
    private String wishText;

    @ApiModelProperty(position = 18, value = "좋아요 혹은 싫어요한 멤버로 추가한 시간", example = "시간")
    private LocalDateTime addedAt;

    public MatchingDefaultResponseDto(MatchingInfo matchingInfo) {
        this.memberId = matchingInfo.getMember().getId();
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
    }

    public MatchingDefaultResponseDto(MatchingInfo matchingInfo, LocalDateTime addedAt) {
        this.memberId = matchingInfo.getMember().getId();
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
        this.addedAt = addedAt;
    }
}
