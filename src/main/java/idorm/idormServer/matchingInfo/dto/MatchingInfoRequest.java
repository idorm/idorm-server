package idorm.idormServer.matchingInfo.dto;

import idorm.idormServer.common.ValidationSequence;
import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.matchingInfo.domain.Gender;
import idorm.idormServer.matchingInfo.domain.JoinPeriod;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({MatchingInfoRequest.class,
        ValidationSequence.NotBlank.class,
        ValidationSequence.NotNull.class,
        ValidationSequence.Size.class,
        ValidationSequence.Range.class
})
@Schema(title = "온보딩 정보 저장 및 수정 요청")
public class MatchingInfoRequest {

    @Schema(required = true, description = "기숙사 분류", allowableValues = "DORM1, DORM2, DORM3",
            example = "DORM1")
    @NotBlank(message = "기숙사 분류를 입력해 주세요.", groups = ValidationSequence.NotBlank.class)
    private String dormCategory;

    @Schema(required = true, description = "입사기간", allowableValues = "WEEK16, WEEK24",
            example = "WEEK16")
    @NotBlank(message = "입사기간을 입력해 주세요.", groups = ValidationSequence.NotBlank.class)
    private String joinPeriod;

    @Schema(required = true, description = "성별", allowableValues = "MALE, FEMALE", example = "MALE")
    @NotBlank(message = "성별을 입력해주세요.", groups = ValidationSequence.NotBlank.class)
    private String gender;

    @Schema(required = true, example = "20", description = "나이")
    @NotNull(message = "나이 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    @Range(min = 20, max = 100, message = "나이는 20~100살 이내여야 합니다.", groups = ValidationSequence.Range.class)
    private Integer age;

    @Schema(required = true, example = "true", description = "코골이 여부")
    @NotNull(message = "코골이 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private Boolean isSnoring;

    @Schema(required = true, example = "true", description = "이갈이 여부")
    @NotNull(message = "이갈이 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private Boolean isGrinding;

    @Schema(required = true, example = "true", description = "흡연 여부")
    @NotNull(message = "흡연 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private Boolean isSmoking;

    @Schema(required = true, example = "true", description = "실내 음식 섭취 여부")
    @NotNull(message = "실내 음식 허용 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private Boolean isAllowedFood;

    @Schema(required = true, example = "true", description = "이어폰 착용 여부")
    @NotNull(message = "이어폰 착용 의사 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private Boolean isWearEarphones;

    @Schema(required = true, example = "오전 9시에 기상합니다.", description = "기상시간")
    @NotBlank(message = "기상 시간 입력은 필수입니다.", groups = ValidationSequence.NotBlank.class)
    @Size(max = 30, message = "기상 시간은 ~30자 이내여야 합니다.", groups = ValidationSequence.Size.class)
    private String wakeupTime;

    @Schema(required = true, example = "밤 10시에 늘 청소해요.", description = "정리정돈")
    @NotBlank(message = "정리정돈 상태 입력은 필수입니다.", groups = ValidationSequence.NotBlank.class)
    @Size(max = 30, message = "정리정돈 상태는 ~30자 이내여야 합니다.", groups = ValidationSequence.Size.class)
    private String cleanUpStatus;

    @Schema(required = true, example = "아침 8시, 밤 12시에 주로 씻어요.", description = "샤워시간")
    @NotBlank(message = "샤워 시간 입력은 필수입니다.", groups = ValidationSequence.NotBlank.class)
    @Size(max = 30, message = "샤워시간은 ~30자 이내여야 합니다.", groups = ValidationSequence.Size.class)
    private String showerTime;

    @Schema(required = true, example = "https://open.kakao.com/o/szaIN6ze", description = "오픈채팅 링크")
    @NotBlank(message = "오픈 채팅 링크 입력은 필수입니다.", groups = ValidationSequence.NotBlank.class)
    @Size(max = 100, message = "오픈채팅 링크는 ~100자 이내여야 합니다.", groups = ValidationSequence.Size.class)
    private String openKakaoLink;

    @Schema(example = "ISTP", description = "MBTI")
    @Size(max = 4, message = "mbti는 ~4자 여야 합니다.", groups = ValidationSequence.Size.class)
    private String mbti;

    @Schema(example = "적당한 선을 지키면서 친해질 수 있는 룸메이트 구해요 :)", description = "룸메에게 보내는 한마디")
    @Size(max = 150, message = "하고싶은 말은 ~150자 이내여야 합니다.", groups = ValidationSequence.Size.class)
    private String wishText;


    public MatchingInfo toEntity(Member member) {
        return MatchingInfo.builder()
                .dormCategory(DormCategory.validateType(this.dormCategory))
                .joinPeriod(JoinPeriod.validateType(this.joinPeriod))
                .gender(Gender.validateType(this.gender))
                .age(this.age)
                .isSnoring(this.isSnoring)
                .isSmoking(this.isSmoking)
                .isAllowedFood(this.isAllowedFood)
                .isWearEarphones(this.isWearEarphones)
                .isGrinding(this.isGrinding)
                .wakeUpTime(this.wakeupTime)
                .cleanUpStatus(this.cleanUpStatus)
                .showerTime(this.showerTime)
                .openKakaoLink(this.openKakaoLink)
                .mbti(mbti.toUpperCase())
                .wishText(this.wishText)
                .member(member)
                .build();
    }
}