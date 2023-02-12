package idorm.idormServer.matchingInfo.dto;

import idorm.idormServer.common.ValidationSequence;
import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.matchingInfo.domain.JoinPeriod;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.matchingInfo.domain.Gender;
import idorm.idormServer.member.domain.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@GroupSequence({MatchingInfoDefaultRequestDto.class,
        ValidationSequence.NotBlank.class,
        ValidationSequence.NotNull.class,
        ValidationSequence.Size.class,
        ValidationSequence.Range.class
})
@ApiModel(value = "MatchingInfo 기본 요청")
public class MatchingInfoDefaultRequestDto {

    @ApiModelProperty(position = 1, required = true, value = "기숙사 분류", allowableValues = "DORM1, DORM2, DORM3",
            example = "DORM1")
    @NotBlank(message = "기숙사 분류를 입력해 주세요.", groups = ValidationSequence.NotBlank.class)
    private String dormCategory;

    @ApiModelProperty(position = 2, required = true, value = "입사기간", allowableValues = "WEEK16, WEEK24",
            example = "WEEK16")
    @NotBlank(message = "입사기간을 입력해 주세요.", groups = ValidationSequence.NotBlank.class)
    private String joinPeriod;

    @ApiModelProperty(position = 3, required = true, value = "성별", allowableValues = "MALE, FEMALE", example = "MALE")
    @NotBlank(message = "성별을 입력해주세요.", groups = ValidationSequence.NotBlank.class)
    private String gender;

    @ApiModelProperty(position = 4, required = true, example = "20", value = "나이")
    @NotNull(message = "나이 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    @Range(min = 20, max = 100, message = "나이는 20~100살 사이여야 합니다.", groups = ValidationSequence.Range.class)
    private Integer age;

    @ApiModelProperty(position = 5, required = true, example = "true", value = "코골이 여부")
    @NotNull(message = "코골이 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private Boolean isSnoring;

    @ApiModelProperty(position = 6, required = true, example = "true", value = "이갈이 여부")
    @NotNull(message = "이갈이 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private Boolean isGrinding;

    @ApiModelProperty(position = 7, required = true, example = "true", value = "흡연 여부")
    @NotNull(message = "흡연 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private Boolean isSmoking;

    @ApiModelProperty(position = 8, required = true, example = "true", value = "실내 음식 섭취 여부")
    @NotNull(message = "실내 음식 허용 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private Boolean isAllowedFood;

    @ApiModelProperty(position = 9, required = true, example = "true", value = "이어폰 착용 여부")
    @NotNull(message = "이어폰 착용 의사 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private Boolean isWearEarphones;

    @ApiModelProperty(position = 10, required = true, example = "오전 9시에 기상합니다.", value = "기상시간")
    @NotBlank(message = "기상 시간 입력은 필수입니다.", groups = ValidationSequence.NotBlank.class)
    @Size(max = 30, message = "기상 시간은 ~30자 이내로 입력해 주세요.", groups = ValidationSequence.Size.class)
    private String wakeupTime;

    @ApiModelProperty(position = 11, required = true, example = "밤 10시에 늘 청소해요.", value = "정리정돈")
    @NotBlank(message = "정리정돈 상태 입력은 필수입니다.", groups = ValidationSequence.NotBlank.class)
    @Size(max = 30, message = "정리정돈 상태는 ~30자 이내로 입력해 주세요.", groups = ValidationSequence.Size.class)
    private String cleanUpStatus;

    @ApiModelProperty(position = 12, required = true, example = "아침 8시, 밤 12시에 주로 씻어요.", value = "샤워시간")
    @NotBlank(message = "샤워 시간 입력은 필수입니다.", groups = ValidationSequence.NotBlank.class)
    @Size(max = 30, message = "샤워시간은 ~30자 이내로 입력해 주세요.", groups = ValidationSequence.Size.class)
    private String showerTime;

    @ApiModelProperty(position = 13, required = true, example = "https://open.kakao.com/o/szaIN6ze", value = "오픈채팅 링크")
    @NotBlank(message = "오픈 채팅 링크 입력은 필수입니다.", groups = ValidationSequence.NotBlank.class)
    @Size(max = 100, message = "오픈채팅 링크는 ~100자 이내로 입력해 주세요.", groups = ValidationSequence.Size.class)
    private String openKakaoLink;

    @ApiModelProperty(position = 14, example = "ISTP", value = "MBTI")
    @Size(min = 3, max = 5, message = "mbti는 3~5자로 입력해 주세요.", groups = ValidationSequence.Size.class)
    private String mbti;

    @ApiModelProperty(position = 15, example = "적당한 선을 지키면서 친해질 수 있는 룸메이트 구해요 :)", value = "룸메에게 보내는 한마디")
    @Size(max = 150, message = "하고싶은 말은 ~150자 이내로 입력해 주세요.", groups = ValidationSequence.Size.class)
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
                .mbti(this.mbti)
                .wishText(this.wishText)
                .member(member)
                .build();
    }
}