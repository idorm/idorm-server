package idorm.idormServer.matchingInfo.domain;

import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.matchingInfo.dto.MatchingInfoDefaultRequestDto;
import idorm.idormServer.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingInfo extends BaseEntity {

    @Id
    @Column(name="matching_info_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Character dormCategory;
    private Character joinPeriod;
    private Character gender;
    private Integer age;
    private Boolean isSnoring;
    private Boolean isGrinding;
    private Boolean isSmoking;
    private Boolean isAllowedFood;
    private Boolean isWearEarphones;
    private String wakeUpTime;
    private String cleanUpStatus;
    private String showerTime;
    private String openKakaoLink;
    private String mbti;
    private String wishText;
    private Boolean isMatchingInfoPublic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Builder
    public MatchingInfo(DormCategory dormCategory,
                        JoinPeriod joinPeriod,
                        Gender gender,
                        Integer age,
                        Boolean isSnoring,
                        Boolean isSmoking,
                        Boolean isGrinding,
                        Boolean isWearEarphones,
                        Boolean isAllowedFood,
                        String wakeUpTime,
                        String cleanUpStatus,
                        String showerTime,
                        String mbti,
                        String wishText,
                        String openKakaoLink,
                        Member member) {
        this.dormCategory = dormCategory.getType();
        this.joinPeriod = joinPeriod.getType();
        this.gender = gender.getType();
        this.age = age;
        this.isSnoring = isSnoring;
        this.isSmoking = isSmoking;
        this.isGrinding = isGrinding;
        this.isWearEarphones = isWearEarphones;
        this.isAllowedFood = isAllowedFood;
        this.wakeUpTime = wakeUpTime;
        this.cleanUpStatus = cleanUpStatus;
        this.showerTime = showerTime;
        this.mbti = mbti;
        this.wishText = wishText;
        this.openKakaoLink = openKakaoLink;
        this.isMatchingInfoPublic = false;
        this.member = member;

        this.setIsDeleted(false);

        if (!member.getMatchingInfos().contains(this))
            member.getMatchingInfos().add(this);
    }

    public void updateMatchingInfo(MatchingInfoDefaultRequestDto requestDto) {
        this.dormCategory = DormCategory.validateType(requestDto.getDormCategory()).getType();
        this.joinPeriod = JoinPeriod.validateType(requestDto.getJoinPeriod()).getType();
        this.gender = Gender.validateType(requestDto.getGender()).getType();
        this.age = requestDto.getAge();
        this.isSnoring = requestDto.getIsSnoring();
        this.isSmoking = requestDto.getIsSmoking();
        this.isGrinding = requestDto.getIsGrinding();
        this.isWearEarphones = requestDto.getIsWearEarphones();
        this.isAllowedFood = requestDto.getIsAllowedFood();
        this.wakeUpTime = requestDto.getWakeupTime();
        this.cleanUpStatus = requestDto.getCleanUpStatus();
        this.showerTime = requestDto.getShowerTime();
        this.mbti = requestDto.getMbti();
        this.wishText = requestDto.getWishText();
        this.openKakaoLink = requestDto.getOpenKakaoLink();
    }

    public void updateIsMatchingInfoPublic(Boolean isMatchingInfoPublic) {
        this.isMatchingInfoPublic = isMatchingInfoPublic;
    }

    public void delete() {
        this.setIsDeleted(true);
    }
}
