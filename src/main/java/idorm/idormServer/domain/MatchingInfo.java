package idorm.idormServer.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingInfo {

    @Id @GeneratedValue
    @Column(name="matchingInfo_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Dormitory dormNum; // 기숙사 선택 [기숙사1,기숙사2,기숙사3]

    @Enumerated(EnumType.STRING)
    private JoinPeriod joinPeriod; // 입사기간 [WEEK16,WEEK24]

    @Enumerated(EnumType.STRING)
    private Gender gender; // 본인의 성별 [FEMALE, MALE]

    private Integer age;

    private Boolean isSnoring;// 코골이 여부
    private Boolean isGrinding; // 이갈이 여부
    private Boolean isSmoking; // 흡연 여부
    private Boolean isAllowedFood; // 음식 허용 여부
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
    @OneToOne(mappedBy = "matchingInfo")
    private Member member;


    /**
     * 생성 메서드
     */
    public MatchingInfo(Dormitory dormNum, JoinPeriod joinPeriod, Gender gender, Integer age,
                        Boolean isSnoring, Boolean isSmoking, Boolean isGrinding, Boolean isWearEarphones,
                        Boolean isAllowedFood, String wakeUpTime, String cleanUpStatus, String showerTime,
                        String mbti, String wishText, String openKakaoLink) {
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
    }

    /**
     * 핵심 비지니스 로직
     */
    public void updateAllMatchingInfo(Dormitory dormNum, JoinPeriod joinPeriod, Gender gender, Integer age,
                                   Boolean isSnoring, Boolean isSmoking, Boolean isGrinding, Boolean isWearEarphones,
                                   Boolean isAllowedFood, String wakeUpTime, String cleanUpStatus, String showerTime,
                                   String mbti, String wishText, String openKakaoLink) {
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
    }

    public void updateDormNum(Dormitory dormNum) {
        this.dormNum = dormNum;
    }

    public void updateJoinPeriod(JoinPeriod joinPeriod) {
        this.joinPeriod = joinPeriod;
    }

    public void updateGender(Gender gender) {
        this.gender = gender;
    }

    public void updateAge(Integer age) {
        this.age = age;
    }

    public void updateIsSnoring(Boolean isSnoring) {
        this.isSnoring = isSnoring;
    }

    public void updateIsSmoking(Boolean isSmoking) {
        this.isSmoking = isSmoking;
    }
    public void updateIsGrinding(Boolean isGrinding) {
        this.isGrinding = isGrinding;
    }
    public void updateIsWearEarphones(Boolean isWearEarphones) {
        this.isWearEarphones = isWearEarphones;
    }
    public void updateIsAllowedFood(Boolean isAllowedFood) {
        this.isAllowedFood = isAllowedFood;
    }
    public void updateWakeupTime(String wakeUpTime) {
        this.wakeUpTime = wakeUpTime;
    }
    public void updateCleanUpStatus(String cleanUpStatus) {
        this.cleanUpStatus = cleanUpStatus;
    }

    public void updateShowerTime(String showerTime) {
        this.showerTime = showerTime;
    }

    public void updateMbti(String mbti) {
        this.mbti = mbti;
    }

    public void updateWishtext(String wishText) {
        this.wishText = wishText;
    }

    public void updateOpenKakaoLink(String openKakaoLink) {
        this.openKakaoLink = openKakaoLink;
    }

}
