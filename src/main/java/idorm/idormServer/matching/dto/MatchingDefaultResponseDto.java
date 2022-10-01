package idorm.idormServer.matching.dto;

import idorm.idormServer.matchingInfo.domain.MatchingInfo;

import idorm.idormServer.member.domain.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "Matching 응답")
@AllArgsConstructor
@Builder
public class MatchingDefaultResponseDto {

    @ApiModelProperty(position = 1, value="멤버 식별자")
    private Long memberId;

    @ApiModelProperty(position = 2, value="매칭정보 식별자")
    private Long matchingInfoId;

    @ApiModelProperty(position = 3, example = "DORM1, DORM2, DORM3")
    private String dormNum;

    @ApiModelProperty(position = 4, example = "WEEK16, WEEK24")
    private String joinPeriod;

    @ApiModelProperty(position = 5, example = "FEMALE, MALE")
    private String gender;

    @ApiModelProperty(position = 6, value = "나이")
    private Integer age;

    @ApiModelProperty(position = 7, value = "코골이 여부")
    private Boolean isSnoring;

    @ApiModelProperty(position = 8, value = "이갈이 여부")
    private Boolean isGrinding;

    @ApiModelProperty(position = 9, value = "흡연 여부")
    private Boolean isSmoking;

    @ApiModelProperty(position = 10, value = "실내 음식 허용 여부")
    private Boolean isAllowedFood;

    @ApiModelProperty(position = 11, value = "이어폰 착용 여부")
    private Boolean isWearEarphones;

    @ApiModelProperty(position = 12, value = "기상 시간")
    private String wakeUpTime;

    @ApiModelProperty(position = 13, value = "정리 정돈 상태")
    private String cleanUpStatus;

    @ApiModelProperty(position = 14, value = "샤워 시간")
    private String showerTime;

    @ApiModelProperty(position = 15, value = "오픈 채팅 링크")
    private String openKakaoLink;

    @ApiModelProperty(position = 16, value = "ISTP")
    private String mbti;

    @ApiModelProperty(position = 17, value = "룸메에게 하고싶은 말")
    private String wishText;

    @ApiModelProperty(position = 18, value = "매칭이미지 공개 여부")
    private Boolean isMatchingInfoPublic;

    @ApiModelProperty(position = 19, value = "aaa@inu.ac.kr")
    private String memberEmail;

    @ApiModelProperty(position = 20, value = "좋아요한 룸메이트 아이디")
    private List<Long> likedMemberId;

    public MatchingDefaultResponseDto(MatchingInfo matchingInfo) {
        this.memberId = matchingInfo.getMember().getId();
        this.matchingInfoId = matchingInfo.getId();
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

        // 문제 코드 NullPointerException
//        List<Member> likedMembers = matchingInfo.getMember().getLikedMembers();
//        for(Member likedMember : likedMembers) {
//            this.likedMemberId.add(likedMember.getId());
//        }
    }
}
