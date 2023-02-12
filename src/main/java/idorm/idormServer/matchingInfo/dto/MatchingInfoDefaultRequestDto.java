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

    @ApiModelProperty(position = 1, required = true, value = "기숙사 분류: DORM1, DORM2, DORM3", example = "DORM1")
    @NotNull(message = "기숙사 분류를 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private DormCategory dormCategory;

    @ApiModelProperty(position = 2, required = true, value = "입사기간: WEEK16, WEEK24", example = "WEEK16")
    @NotNull(message = "입사기간을 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private JoinPeriod joinPeriod;

    @ApiModelProperty(position = 3, required = true, value = "성별: MALE, FEMALE", example = "MALE")
    @NotNull(message = "성별을 입력해주세요.", groups = ValidationSequence.NotNull.class)
    private Gender gender;

    @ApiModelProperty(position = 4, required = true, value = "나이", example = "20")
    @NotNull(message = "나이 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    @Range(min = 20, max = 100, message = "나이는 20~100 살 사이여야 합니다.", groups = ValidationSequence.Range.class)
    private Integer age;

    @ApiModelProperty(position = 5, required = true, value = "코골이 여부", example = "false")
    @NotNull(message = "코골이 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private Boolean isSnoring;

    @ApiModelProperty(position = 6, required = true, value = "흡연 여부", example = "false")
    @NotNull(message = "흡연 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private Boolean isSmoking;

    @ApiModelProperty(position = 7, required = true, value = "이갈이 여부", example = "false")
    @NotNull(message = "이갈이 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private Boolean isGrinding;

    @ApiModelProperty(position = 8, required = true, value = "이어폰 착용 의사 여부", example = "false")
    @NotNull(message = "이어폰 착용 의사 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private Boolean isWearEarphones;

    @ApiModelProperty(position = 9, required = true, value = "실내 음식 섭취 허용 여부", example = "false")
    @NotNull(message = "실내 음식 허용 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private Boolean isAllowedFood;

    @ApiModelProperty(position = 10, required = true, value = "기상 시간", example = "주로 아침 8시에 일어납니다.")
    @NotBlank(message = "기상시간을 입력해 주세요.", groups = ValidationSequence.NotBlank.class)
    private String wakeupTime;

    @ApiModelProperty(position = 11, required = true, value = "청소 상태", example = "적당히 깨끗한 걸 좋아해요.")
    @NotBlank(message = "청소 상태를 입력해 주세요.", groups = ValidationSequence.NotBlank.class)
    private String cleanUpStatus;

    @ApiModelProperty(position = 12, required = true, value = "샤워 시간", example = "주로 아침 8시 30분, 밤 11시에 씻어요.")
    @NotBlank(message = "샤워 시간을 입력해 주세요.", groups = ValidationSequence.NotBlank.class)
    private String showerTime;

    @ApiModelProperty(position = 13, value = "mbti", example = "ISTP")
    @Size(min = 3, max = 5, message = "mbti는 3~5자로 입력해 주세요.", groups = ValidationSequence.Size.class)
    private String mbti;

    @ApiModelProperty(position = 14, value = "룸메에게 하고싶은 말", example = "주말에는 주로 본가에 갑니다. 함께 잘 지내봐요!")
    @Size(min = 1, max = 50, message = "하고싶은 말은 1~50자로 입력해 주세요.", groups = ValidationSequence.Size.class)
    private String wishText;

    @ApiModelProperty(position = 15, value = "오픈채팅 링크", example = "https://open.kakao.com/o/szaIN6ze")
    @NotBlank(message = "오픈 채팅 링크를 입력해 주세요.", groups = ValidationSequence.NotBlank.class)
    private String openKakaoLink;

    public MatchingInfo toEntity(Member member) {
        return MatchingInfo.builder()
                .dormCategory(this.dormCategory)
                .joinPeriod(this.joinPeriod)
                .gender(this.gender)
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