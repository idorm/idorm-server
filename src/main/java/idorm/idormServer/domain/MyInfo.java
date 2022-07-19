package idorm.idormServer.domain;

import idorm.idormServer.dto.MyInfoDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyInfo {

    @Id @GeneratedValue
    @Column(name="myinfo_id")
    private Long id;

    private Dormitory dormNum; // 기숙사 선택 [1기숙사, 2기숙사, 3기숙사]

    private JoinPeriod joinPeriod; // 입사기간 [24주, 16주]

    @Enumerated(EnumType.STRING)
    private Gender gender; // 본인의 성별

    private Integer age;

    private Boolean isSnoring; // 코골이 여부
    private Boolean isSmoking; // 흡연 여부
    private Boolean isGrinding; // 이갈이 여부
    private Boolean isWearEarphones; // 이어폰 착용 의사 여부
    private Boolean isAllowedFood; // 음식 허용 여부

    private String wakeUpTime; // 기상 시간
    private String cleanUpStatus; // 정리정돈 상태
    private String showerTime; // 샤워 시간

    private String MBTI;
    private String wishText; // 하고 싶은 말


    @OneToOne(mappedBy = "myInfo")
    @JoinColumn(name="member_id")
    private Member member;


    // 생성 메서드

    public MyInfo(Dormitory dormNum, JoinPeriod joinPeriod, Gender gender, Integer age,
                  Boolean isSnoring, Boolean isSmoking, Boolean isGrinding, Boolean isWearEarphones,
                  Boolean isAllowedFood, String wakeUpTime, String cleanUpStatus, String showerTime,
                  String MBTI, String wishText) {
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
        this.MBTI = MBTI;
        this.wishText = wishText;
    }

    public MyInfo updateMyInfo(MyInfoDTO myInfoDTO) {
        this.dormNum = myInfoDTO.getDormNum();
        this.joinPeriod = myInfoDTO.getJoinPeriod();
        this.isSnoring = myInfoDTO.getIsSnoring();
        this.isSmoking = myInfoDTO.getIsSmoking();
        this.isGrinding = myInfoDTO.getIsGrinding();
        this.isWearEarphones = myInfoDTO.getIsWearEarphones();
        this.isAllowedFood = myInfoDTO.getIsAllowedFood();
        this.wakeUpTime = myInfoDTO.getWakeUpTime();
        this.cleanUpStatus = myInfoDTO.getCleanUpStatus();
        this.showerTime = myInfoDTO.getShowerTime();
        this.MBTI = myInfoDTO.getMBTI();
        this.wishText = myInfoDTO.getWishText();

        return this;
    }
}
