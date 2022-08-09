//package idorm.idormServer.domain;
//
//import idorm.idormServer.dto.MatchingInfoDTO;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.*;
//
//@Entity
//@Getter @Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class MatchingInfo {
//
//    @Id @GeneratedValue
//    @Column(name="matchingInfo_id")
//    private Long id;
//
//    private Dormitory dormNum; // 기숙사 선택 [1기숙사, 2기숙사, 3기숙사]
//
//    private JoinPeriod joinPeriod; // 입사기간 [24주, 16주]
//
//    @Enumerated(EnumType.STRING)
//    private Gender gender; // 본인의 성별
//
//    private Integer age;
//
//    private boolean snoring;// 코골이 여부
//    private boolean smoking; // 흡연 여부
//    private boolean grinding; // 이갈이 여부
//    private boolean wearEarphones; // 이어폰 착용 의사 여부
//    private boolean allowedFood; // 음식 허용 여부
//
//    private String wakeUpTime; // 기상 시간
//    private String cleanUpStatus; // 정리정돈 상태
//    private String showerTime; // 샤워 시간
//
//    private String MBTI;
//    private String wishText; // 하고 싶은 말
//    private String chatLink; // 오픈채팅 링크
//
//
//    @OneToOne
//    @JoinColumn(name="member_id")
//    private Member member;
//
//
//    // 생성 메서드
//
//    public MatchingInfo(Dormitory dormNum, JoinPeriod joinPeriod, Gender gender, Integer age,
//                        boolean isSnoring, boolean isSmoking, boolean isGrinding, boolean isWearEarphones,
//                        boolean isAllowedFood, String wakeUpTime, String cleanUpStatus, String showerTime,
//                        String MBTI, String wishText) {
//        this.dormNum = dormNum;
//        this.joinPeriod = joinPeriod;
//        this.gender = gender;
//        this.age = age;
//        this.snoring = isSnoring;
//        this.smoking = isSmoking;
//        this.grinding = isGrinding;
//        this.wearEarphones = isWearEarphones;
//        this.allowedFood = isAllowedFood;
//        this.wakeUpTime = wakeUpTime;
//        this.cleanUpStatus = cleanUpStatus;
//        this.showerTime = showerTime;
//        this.MBTI = MBTI;
//        this.wishText = wishText;
//    }
//
//    public MatchingInfo updateMatchingInfo(MatchingInfoDTO matchingInfoDTO) {
//        this.dormNum = matchingInfoDTO.getDormNum();
//        this.joinPeriod = matchingInfoDTO.getJoinPeriod();
//        this.gender = matchingInfoDTO.getGender();
//        this.age = matchingInfoDTO.getAge();
//        this.snoring = matchingInfoDTO.isSnoring();
//        this.smoking = matchingInfoDTO.isSmoking();
//        this.grinding = matchingInfoDTO.isGrinding();
//        this.wearEarphones = matchingInfoDTO.isWearEarphones();
//        this.allowedFood = matchingInfoDTO.isAllowedFood();
//        this.wakeUpTime = matchingInfoDTO.getWakeUpTime();
//        this.cleanUpStatus = matchingInfoDTO.getCleanUpStatus();
//        this.showerTime = matchingInfoDTO.getShowerTime();
//        this.MBTI = matchingInfoDTO.getMBTI();
//        this.wishText = matchingInfoDTO.getWishText();
//
//        return this;
//    }
//}
