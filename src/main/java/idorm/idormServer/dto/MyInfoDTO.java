package idorm.idormServer.dto;

import idorm.idormServer.domain.Dormitory;
import idorm.idormServer.domain.Gender;
import idorm.idormServer.domain.JoinPeriod;
import idorm.idormServer.domain.MyInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MyInfoDTO {

    private Long id;
    private Dormitory dormNum;
    private JoinPeriod joinPeriod;
    private Gender gender;
    private Integer age;

    private Boolean isSnoring; // 코골이 여부
    private Boolean isSmoking; // 흡연 여부
    private Boolean isGrinding; // 이갈이 여부
    private Boolean isWearEarphones;
    private Boolean isAllowedFood;

    private String wakeUpTime; // 기상 시간
    private String cleanUpStatus; // 정리정돈 상태
    private String showerTime; // 샤워 시간

    private String MBTI;
    private String wishText; // 하고 싶은 말

    private Long memberId;
    private String memberEmail;


    public MyInfoDTO(MyInfo myInfo) {

        // TODO: 넘겨줘야 할 값만 설정, 나머지는 빼기
        this.id = myInfo.getId();
        this.dormNum = myInfo.getDormNum();
        this.joinPeriod = myInfo.getJoinPeriod();
        this.gender = myInfo.getGender();
        this.age = myInfo.getAge();
        this.isSnoring = myInfo.getIsSnoring();
        this.isSmoking = myInfo.getIsSmoking();
        this.isGrinding = myInfo.getIsGrinding();
        this.isWearEarphones = myInfo.getIsWearEarphones();
        this.isAllowedFood = myInfo.getIsAllowedFood();
        this.wakeUpTime = myInfo.getWakeUpTime();
        this.cleanUpStatus = myInfo.getCleanUpStatus();
        this.showerTime = myInfo.getShowerTime();
        this.MBTI = myInfo.getMBTI();
        this.wishText = myInfo.getWishText();

        this.memberId = myInfo.getMember().getId();
        this.memberEmail = myInfo.getMember().getEmail();
    }

    public static class CreateMyInfoResponse {
        private Long id;

        public CreateMyInfoResponse(Long id) {
            this.id = id;
        }
    }

    public static class createMyInfoRequest {

        private Dormitory dormNum;
        private JoinPeriod joinPeriod;
        private Gender gender;
        private Integer age;

        private boolean isSnoring; // 코골이 여부
        private boolean isSmoking; // 흡연 여부
        private boolean isGrinding; // 이갈이 여부
        private Boolean isWearEarphones;
        private Boolean isAllowedFood;

        private String wakeUpTime; // 기상 시간
        private String cleanUpStatus; // 정리정돈 상태
        private String showerTime; // 샤워 시간

        private String MBTI;
        private String wishText; // 하고 싶은 말
    }

    @AllArgsConstructor
    public static class DeleteMyInfo{
        private Long id;
    }

}
