package idorm.idormServer.dto;

import idorm.idormServer.domain.Dormitory;
import idorm.idormServer.domain.Gender;
import idorm.idormServer.domain.JoinPeriod;
import idorm.idormServer.domain.MatchingInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class MatchingInfoDTO {

    private Long id;
    private Dormitory dormNum;
    private JoinPeriod joinPeriod;
    private Gender gender;
    private Integer age;

    private boolean snoring; // 코골이 여부
    private boolean smoking; // 흡연 여부
    private boolean grinding; // 이갈이 여부
    private boolean wearEarphones;
    private boolean allowedFood;

    private String wakeUpTime; // 기상 시간
    private String cleanUpStatus; // 정리정돈 상태
    private String showerTime; // 샤워 시간

    private String MBTI;
    private String wishText; // 하고 싶은 말

    private Long memberId;
    private String memberEmail;


    public MatchingInfoDTO(MatchingInfo myInfo) {

        // TODO: 넘겨줘야 할 값만 설정, 나머지는 빼기
        this.id = myInfo.getId();
        this.dormNum = myInfo.getDormNum();
        this.joinPeriod = myInfo.getJoinPeriod();
        this.gender = myInfo.getGender();
        this.age = myInfo.getAge();
        this.snoring = myInfo.isSnoring();
        this.smoking = myInfo.isSmoking();
        this.grinding = myInfo.isGrinding();
        this.wearEarphones = myInfo.isWearEarphones();
        this.allowedFood = myInfo.isAllowedFood();
        this.wakeUpTime = myInfo.getWakeUpTime();
        this.cleanUpStatus = myInfo.getCleanUpStatus();
        this.showerTime = myInfo.getShowerTime();
        this.MBTI = myInfo.getMBTI();
        this.wishText = myInfo.getWishText();

        this.memberId = myInfo.getMember().getId();
        this.memberEmail = myInfo.getMember().getEmail();
    }

    public static class CreateMatchingInfoResponse {
        private Long id;

        public CreateMatchingInfoResponse(Long id) {
            this.id = id;
        }
    }

    public static class createMatchingInfoRequest {

        private Dormitory dormNum;
        private JoinPeriod joinPeriod;
        private Gender gender;
        private Integer age;

        private boolean snoring; // 코골이 여부
        private boolean smoking; // 흡연 여부
        private boolean grinding; // 이갈이 여부
        private boolean wearEarphones;
        private boolean allowedFood;

        private String wakeUpTime; // 기상 시간
        private String cleanUpStatus; // 정리정돈 상태
        private String showerTime; // 샤워 시간

        private String MBTI;
        private String wishText; // 하고 싶은 말
    }

    @AllArgsConstructor
    public static class DeleteMatchingInfo{
        private Long id;
    }

}
