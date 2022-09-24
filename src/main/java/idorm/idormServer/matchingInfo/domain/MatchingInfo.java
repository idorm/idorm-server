package idorm.idormServer.matchingInfo.domain;

import idorm.idormServer.common.BaseEntity;
import idorm.idormServer.matchingInfo.dto.MatchingInfoDefaultRequestDto;
import idorm.idormServer.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingInfo extends BaseEntity {

    @Id
    @Column(name="matching_info_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isMatchingInfoPublic; // 매칭이미지 공개 여부

//    @Enumerated(EnumType.STRING)
//    private Dormitory dormNum; // 기숙사 선택 [DORM1,DORM2,DORM3]
//
//    @Enumerated(EnumType.STRING)
//    private JoinPeriod joinPeriod; // 입사기간 [WEEK16,WEEK24]
//
//    @Enumerated(EnumType.STRING)
//    private Gender gender; // 성별 [FEMALE, MALE]


    private String dormNum; // 기숙사 선택 [DORM1,DORM2,DORM3]

    private String joinPeriod; // 입사기간 [WEEK16,WEEK24]

    private String gender; // 성별 [FEMALE, MALE]

    private Integer age;

    private Boolean isSnoring;// 코골이 여부

    private Boolean isGrinding; // 이갈이 여부
    private Boolean isSmoking; // 흡연 여부
    private Boolean isAllowedFood; // 실내 음식 허용 여부
    private Boolean isWearEarphones; // 이어폰 착용 의사 여부

    private String wakeUpTime; // 기상 시간
    private String cleanUpStatus; // 정리정돈 상태
    private String showerTime; // 샤워 시간

    private String openKakaoLink; // 오픈채팅 링크
    private String mbti;

    @Size(max=100)
    private String wishText; // 하고 싶은 말

    /**
     * 연관관계 매핑
     */
    @OneToOne
    @JoinColumn(name="member_id")
    private Member member;

    /**
     * 생성 메서드
     */
    @Builder
    public MatchingInfo(String dormNum,
                        String joinPeriod,
                        String gender,
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

        this.isMatchingInfoPublic = false;
        this.dormNum = dormNum;
        this.joinPeriod = joinPeriod;
        this.gender = gender;
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
        this.member = member;
    }

    /**
     * 핵심 비지니스 로직
     */
    public void updateIsMatchingInfoPublic() {
        this.isMatchingInfoPublic = (isMatchingInfoPublic == false) ? true : false;
    }

    public void updateMatchingInfo(MatchingInfoDefaultRequestDto requestDto) {

        this.dormNum = requestDto.getDormNum();
        this.joinPeriod = requestDto.getJoinPeriod();
        this.gender = requestDto.getGender();
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

}
