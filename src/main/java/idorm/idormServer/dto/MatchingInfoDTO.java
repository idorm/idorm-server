package idorm.idormServer.dto;

import idorm.idormServer.domain.*;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "온보딩 정보 DTO")
@Getter
@NoArgsConstructor
public class MatchingInfoDTO {

    @Schema(description = "온보딩 정보 저장 DTO")
    public static class MatchingInfoSaveRequestDTO {

        @Schema(description = "기숙사 분류", defaultValue = "기숙사1", allowableValues = {"기숙사1", "기숙사2"})
        private Dormitory dormNum;

        @ApiModelProperty(
                dataType = "JoinPeriod"
                , allowableValues = "WEEK16,WEEK24"
                , required = true)
        private JoinPeriod joinPeriod;

        @ApiModelProperty(
                dataType = "Gender"
                , allowableValues = "FEMALE, MALE"
                , required = true)
        private Gender gender;

        @ApiModelProperty(
                dataType = "Integer"
                , required = true)
        private Integer age;

        @ApiModelProperty(
                dataType = "Boolean"
                , required = true)
        private Boolean isSnoring;

        @ApiModelProperty(
                dataType = "Boolean"
                , required = true)
        private Boolean isGrinding;

        @ApiModelProperty(
                dataType = "Boolean"
                , required = true)
        private Boolean isSmoking;

        @ApiModelProperty(
                dataType = "Boolean"
                , required = true)
        private Boolean isAllowedFood;

        @ApiModelProperty(
                dataType = "Boolean"
                , required = true)
        private Boolean isWearEarphones;

        @ApiModelProperty(
                dataType = "String"
                , required = true)
        private String wakeUpTime;

        @ApiModelProperty(
                dataType = "String"
                , required = true)
        private String cleanUpStatus;

        @ApiModelProperty(
                dataType = "String"
                , required = true)
        private String showerTime;


        @ApiModelProperty(
                dataType = "String"
                , required = false)
        private String openKakaoLink;

        @ApiModelProperty(
                dataType = "String"
                , required = false)
        private String mbti;

        @ApiModelProperty(
                dataType = "String"
                , required = false)
        private String wishText;

        @ApiModelProperty(
                dataType = "Member"
                , required = true)
        private Member member;

        public MatchingInfo toEntity() {
            return MatchingInfo.builder()
                    .dormNum(dormNum)
                    .joinPeriod(joinPeriod)
                    .gender(gender)
                    .age(age)
                    .isSnoring(isSnoring)
                    .isGrinding(isGrinding)
                    .isAllowedFood(isAllowedFood)
                    .isSmoking(isSmoking)
                    .isWearEarphones(isWearEarphones)
                    .wakeUpTime(wakeUpTime)
                    .cleanUpStatus(cleanUpStatus)
                    .showerTime(showerTime)
                    .openKakaoLink(openKakaoLink)
                    .mbti(mbti)
                    .wishText(wishText)
                    .build();
        }
    }

    public static class MatchingInfoUpdateRequestDTO {

        @ApiParam(defaultValue="기숙사1")
        private Dormitory dormNum;

//        @ApiModelProperty(
//                dataType = "JoinPeriod"
//                ,allowableValues = "WEEK16,WEEK24")
        private JoinPeriod joinPeriod;

//        @ApiModelProperty(
//                dataType = "Gender"
//                ,allowableValues = "FEMALE, MALE")
        private Gender gender;

        @ApiModelProperty(
                dataType = "Integer")
        private Integer age;

        @ApiModelProperty(
                dataType = "Boolean")
        private Boolean isSnoring;

        @ApiModelProperty(
                dataType = "Boolean")
        private Boolean isGrinding;

        @ApiModelProperty(
                dataType = "Boolean")
        private Boolean isSmoking;

        @ApiModelProperty(
                dataType = "Boolean")
        private Boolean isAllowedFood;

        @ApiModelProperty(
                dataType = "Boolean")
        private Boolean isWearEarphones;

        @ApiModelProperty(
                dataType = "String")
        private String wakeUpTime;

        @ApiModelProperty(
                dataType = "String")
        private String cleanUpStatus;

        @ApiModelProperty(
                dataType = "String")
        private String showerTime;


        @ApiModelProperty(
                dataType = "String")
        private String openKakaoLink;

        @ApiModelProperty(
                dataType = "String")
        private String mbti;

        @ApiModelProperty(
                dataType = "String")
        private String wishText;

        public MatchingInfo toEntity() {
            return MatchingInfo.builder()
                    .dormNum(dormNum)
                    .joinPeriod(joinPeriod)
                    .gender(gender)
                    .age(age)
                    .isSnoring(isSnoring)
                    .isGrinding(isGrinding)
                    .isAllowedFood(isAllowedFood)
                    .isSmoking(isSmoking)
                    .isWearEarphones(isWearEarphones)
                    .wakeUpTime(wakeUpTime)
                    .cleanUpStatus(cleanUpStatus)
                    .showerTime(showerTime)
                    .openKakaoLink(openKakaoLink)
                    .mbti(mbti)
                    .wishText(wishText)
                    .build();
        }
    }



    @Data
    @AllArgsConstructor
    public static class MatchingInfoResponseDTO {
        private Long id;
    }

//    @Data
//    public static class MatchingInfoOneDTO {
//
//        private Long id;
//        private Dormitory dormNum;
//        private JoinPeriod joinPeriod;
//        private Gender gender;
//        private Integer age;
//        private Boolean isSnoring;// 코골이 여부
//        private Boolean isGrinding; // 이갈이 여부
//        private Boolean isSmoking; // 흡연 여부
//        private Boolean isAllowedFood; // 음식 허용 여부
//        private Boolean isWearEarphones; // 이어폰 착용 의사 여부
//        private String wakeUpTime; // 기상 시간
//        private String cleanUpStatus; // 정리정돈 상태
//        private String showerTime; // 샤워 시간
//        private String openKakaoLink; // 오픈채팅 링크
//        private String mbti;
//        private String wishText; // 하고 싶은 말
//
//        // member
//        private Long memberId;
//
//        public MatchingInfoOneDTO(MatchingInfo matchingInfo) {
//            this.id = matchingInfo.getId();
//            this.dormNum = matchingInfo.getDormNum();
//            this.joinPeriod = matchingInfo.getJoinPeriod();
//            this.gender = matchingInfo.getGender();
//            this.age = matchingInfo.getAge();
//            this.isSnoring = matchingInfo.getIsSnoring();
//            this.isSmoking = matchingInfo.getIsSmoking();
//            this.isGrinding = matchingInfo.getIsGrinding();
//            this.isWearEarphones = matchingInfo.getIsWearEarphones();
//            this.isAllowedFood = matchingInfo.getIsAllowedFood();
//            this.wakeUpTime = matchingInfo.getWakeUpTime();
//            this.cleanUpStatus = matchingInfo.getCleanUpStatus();
//            this.showerTime = matchingInfo.getShowerTime();
//            this.mbti = matchingInfo.getMbti();
//            this.wishText = matchingInfo.getWishText();
//            this.openKakaoLink = matchingInfo.getOpenKakaoLink();
//
//            this.memberId = matchingInfo.getMember().getId();
//        }
//
//
//    }
//
//
//    @Data
//    public static class ReturnMatchingInfoIdResponse {
//
//        private Long id;
//        public ReturnMatchingInfoIdResponse(Long id) {
//            this.id = id;
//        }
//    }
//
//    @Data
//    public static class CreateMatchingInfoRequest {
//
//        private Dormitory dormNum;
//        private JoinPeriod joinPeriod;
//        private Gender gender;
//        private Integer age;
//
//        private Boolean isSnoring;
//        private Boolean isGrinding;
//        private Boolean isSmoking;
//        private Boolean isAllowedFood;
//        private Boolean isWearEarphones;
//
//        private String wakeUpTime;
//        private String cleanUpStatus;
//        private String showerTime;
//
//        private String openKakaoLink;
//        private String mbti;
//        private String wishText;
//    }
//
//    @Data
//    @AllArgsConstructor
//    public static class DeleteMatchingInfo{
//        private Long id;
//    }

}
