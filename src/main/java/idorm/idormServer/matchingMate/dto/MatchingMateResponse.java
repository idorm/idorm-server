package idorm.idormServer.matchingMate.dto;

import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.matchingInfo.domain.Gender;
import idorm.idormServer.matchingInfo.domain.JoinPeriod;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(title = "Matching 기본 응답")
public class MatchingMateResponse {

    @Schema(description="멤버 식별자", example = "2")
    private Long memberId;

    @Schema(description= "매칭정보 식별자", example = "1")
    private Long matchingInfoId;

    @Schema(required = true, description = "기숙사 분류", allowableValues = "DORM1, DORM2, DORM3", example = "DORM1")
    private DormCategory dormCategory;

    @Schema(required = true, description = "입사기간", allowableValues = "WEEK16, WEEK24", example = "WEEK16")
    private JoinPeriod joinPeriod;

    @Schema(required = true, description = "성별", allowableValues = "MALE, FEMALE", example = "MALE")
    private Gender gender;

    @Schema(required = true, example = "20", description = "나이")
    private Integer age;

    @Schema(description = "코골이 여부", example = "false")
    private Boolean isSnoring;

    @Schema(description = "이갈이 여부", example = "false")
    private Boolean isGrinding;

    @Schema(description = "흡연 여부", example = "false")
    private Boolean isSmoking;

    @Schema(description = "실내 음식 허용 여부", example = "false")
    private Boolean isAllowedFood;

    @Schema(description = "이어폰 착용 여부", example = "false")
    private Boolean isWearEarphones;

    @Schema(description = "기상시간", example = "아침 7시 기상")
    private String wakeUpTime;

    @Schema(description = "정리정돈", example = "더러워용")
    private String cleanUpStatus;

    @Schema(description = "샤워시간", example = "아침7시, 밤 12시에 주로 씻어요")
    private String showerTime;

    @Schema(description = "오픈채팅 링크", example = "링크~")
    private String openKakaoLink;

    @Schema(description = "mbti", example = "ISTP")
    private String mbti;

    @Schema(description = "룸메에게 보내는 한마디", example = "즐거운 한 학기를 보내보아요~")
    private String wishText;

    @Schema(description = "매칭정보 공개 여부", example = "true")
    private Boolean isMatchingInfoPublic;

    public MatchingMateResponse(MatchingInfo matchingInfo) {
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
        this.isMatchingInfoPublic = matchingInfo.getIsMatchingInfoPublic();
    }
}
