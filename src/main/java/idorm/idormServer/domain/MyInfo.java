package idorm.idormServer.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyInfo {

    @Id @GeneratedValue
    @Column(name="myinfo_id")
    private Long id;

    @NotEmpty(message = "기숙사 입력은 필수입니다.")
    private Dormitory dormNum; // 기숙사 선택 [1기숙사, 2기숙사, 3기숙사]

    @NotEmpty(message = "입사기간 입력은 필수입니다.")
    private JoinPeriod joinPeriod; // 입사기간 [24주, 16주]

    @NotEmpty(message = "성별 입력은 필수입니다.")
    @Enumerated(EnumType.STRING)
    private Gender gender; // 본인의 성별

    @NotEmpty(message = "나이 입력은 필수입니다.")
    private Integer age;

    @NotEmpty(message = "코골이 여부 입력은 필수입니다.")
    private Boolean isSnoring; // 코골이 여부
    @NotEmpty(message = "흡연 여부 입력 필수입니다.")
    private Boolean isSmoking; // 흡연 여부
    @NotEmpty(message = "이갈이 여부 입력은 필수입니다.")
    private Boolean isGrinding; // 이갈이 여부
    @NotEmpty(message = "이어폰 착용 의사 여부 입력은 필수입니다.")
    private Boolean isWearEarphones; // 이어폰 착용 의사 여부
    @NotEmpty(message = "음식 허용 여부 입력은 필수입니다.")
    private Boolean isAllowedFood; // 음식 허용 여부

    @NotEmpty(message = "기상 시간 입력은 필수입니다.")
    private String wakeUpTime; // 기상 시간
    @NotEmpty(message = "정리정돈의 정도 여부 입력은 필수입니다.")
    private String cleanUpStatus; // 정리정돈 상태
    @NotEmpty(message = "샤워 시간 입력은 필수입니다.")
    private String showerTime; // 샤워 시간

    private String MBTI;
    private String wishText; // 하고 싶은 말


    @OneToOne
    @JoinColumn(name="member_id")
    private Member member;

    // 생성 메서드
    public MyInfo createMyInfo(Dormitory dormNum, JoinPeriod joinPeriod, Gender gender,
                               Integer age, Boolean isSnoring, Boolean isSmoking,
                               Boolean isGrinding, Boolean isWearEarphones, Boolean isAllowedFood,
                               String wakeUpTime, String cleanUpStatus, String showerTime,
                               String MBTI, String wishText) {

        MyInfo myInfo = new MyInfo();

        myInfo.setDormNum(dormNum);
        myInfo.setJoinPeriod(joinPeriod);
        myInfo.setGender(gender);
        myInfo.setAge(age);
        myInfo.setIsSnoring(isSnoring);
        myInfo.setIsSmoking(isSmoking);
        myInfo.setIsGrinding(isGrinding);
        myInfo.setIsWearEarphones(isWearEarphones);
        myInfo.setIsAllowedFood(isAllowedFood);
        myInfo.setWakeUpTime(wakeUpTime);
        myInfo.setCleanUpStatus(cleanUpStatus);
        myInfo.setShowerTime(showerTime);
        myInfo.setMBTI(MBTI);
        myInfo.setWishText(wishText);

        return myInfo;
    }
}
