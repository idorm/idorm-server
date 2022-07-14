package idorm.idormServer.dto;

import idorm.idormServer.domain.Dormitory;
import idorm.idormServer.domain.Gender;
import idorm.idormServer.domain.JoinPeriod;
import idorm.idormServer.domain.MyInfo;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class MyInfoDTO {
    private Long id;
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


    @Data
    public static class MyInfoOneDTO {
        private Long id;
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

        public MyInfoOneDTO(MyInfo myInfo) {

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



        }
    }
}
