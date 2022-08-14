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

    private String wishText; // 하고 싶은 말

    // member
    private Long memberId;

    public MatchingInfoDTO(MatchingInfo matchingInfo) {

        // TODO: 넘겨줘야 할 값만 설정, 나머지는 빼기
        this.id = matchingInfo.getId();
        this.dormNum = matchingInfo.getDormNum();
        this.joinPeriod = matchingInfo.getJoinPeriod();
        this.gender = matchingInfo.getGender();
        this.age = matchingInfo.getAge();
        this.isSnoring = matchingInfo.getIsSnoring();
        this.isSmoking = matchingInfo.getIsSmoking();
        this.isGrinding = matchingInfo.getIsGrinding();
        this.isWearEarphones = matchingInfo.getIsWearEarphones();
        this.isAllowedFood = matchingInfo.getIsAllowedFood();
        this.wakeUpTime = matchingInfo.getWakeUpTime();
        this.cleanUpStatus = matchingInfo.getCleanUpStatus();
        this.showerTime = matchingInfo.getShowerTime();
        this.mbti = matchingInfo.getMbti();
        this.wishText = matchingInfo.getWishText();
        this.openKakaoLink = matchingInfo.getOpenKakaoLink();

        this.memberId = matchingInfo.getMember().getId();
    }


    @Data
    public static class ReturnMatchingInfoIdResponse {

        private Long id;
        public ReturnMatchingInfoIdResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    public static class CreateMatchingInfoRequest {

        private Dormitory dormNum;
        private JoinPeriod joinPeriod;
        private Gender gender;
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
    }

    @Data
    @AllArgsConstructor
    public static class DeleteMatchingInfo{
        private Long id;
    }

}