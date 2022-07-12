package idorm.idormServer.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyInfo {

    @Id @GeneratedValue
    @Column(name="myinfo_id")
    private Long id;

    private boolean isSmoking; // 흡연 여부
    private boolean isSnoring; // 코골이 여부
    private boolean isGrinding; // 이갈이 여부
    private boolean IsAllowedLateNightSnack; // 야식 허용 여부
    private boolean isWearEarphones; // 이어폰 착용 의사 여부

    private int wakeUpTime; // 기상 시간, 일단은 숫자로만 입력받게
    private int bedTime; // 취침 시간
    private int showerTime; // 샤워 시간

    @Enumerated(EnumType.STRING)
    private cleanUpType cleanUpStatus; // 정리정돈 여부

    private String wishText; // 하고 싶은 말

    @Enumerated(EnumType.STRING)
    private GenderType gender; // 본인의 성별

    private String mbti;
    private int age;

    @OneToOne
    @JoinColumn(name="member_id")
    private Member member;

    // 생성 메서드

    public MyInfo createMyInfo(Member member, boolean isSmoking, boolean isSnoring, boolean isGrinding, boolean isAllowedLateNightSnack, boolean isWearEarphones, GenderType gender, String mbti, int age) {
        MyInfo myInfo = new MyInfo();

        myInfo.setMember(member);
        myInfo.setSmoking(isSmoking);
        myInfo.setSnoring(isSnoring);
        myInfo.setGrinding(isGrinding);
        myInfo.setIsAllowedLateNightSnack(isAllowedLateNightSnack);
        myInfo.setWearEarphones(isWearEarphones);
        myInfo.setGender(gender);
        myInfo.setMbti(mbti);
        myInfo.setAge(age);

        return myInfo;

    }

}
