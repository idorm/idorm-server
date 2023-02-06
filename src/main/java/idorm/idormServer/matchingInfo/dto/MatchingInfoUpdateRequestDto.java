package idorm.idormServer.matchingInfo.dto;

import idorm.idormServer.common.ValidationSequence;
import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.matchingInfo.domain.Gender;
import idorm.idormServer.matchingInfo.domain.JoinPeriod;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@GroupSequence({MatchingInfoUpdateRequestDto.class,
        ValidationSequence.NotBlank.class,
        ValidationSequence.Size.class,
        ValidationSequence.Range.class
})
@ApiModel(value = "MatchingInfo 수정 요청")
public class MatchingInfoUpdateRequestDto {

    @ApiModelProperty(position = 1, required = true, value = "기숙사 분류: DORM1, DORM2, DORM3", example = "DORM1")
    @NotNull(message = "기숙사 분류를 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private DormCategory dormCategory;

    @ApiModelProperty(position = 2, required = true, value = "입사기간: WEEK16, WEEK24", example = "WEEK16")
    @NotNull(message = "입사기간을 입력해 주세요.", groups = ValidationSequence.NotNull.class)
    private JoinPeriod joinPeriod;

    @ApiModelProperty(position = 3, required = true, value = "성별: MALE, FEMALE", example = "MALE")
    @NotNull(message = "성별을 입력해주세요.", groups = ValidationSequence.NotNull.class)
    private Gender gender;

    @ApiModelProperty(position = 4, required = true, example = "20")
    @NotNull(message = "나이 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    @Range(min = 20, max = 100, message = "나이는 20~100 살 사이여야 합니다.", groups = ValidationSequence.Range.class)
    private Integer age;

    @ApiModelProperty(position = 5, required = true, example = "true")
    @NotNull(message = "코골이 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private boolean isSnoring;

    @ApiModelProperty(position = 6, required = true, example = "true")
    @NotNull(message = "이갈이 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private boolean isGrinding;

    @ApiModelProperty(position = 7, required = true, example = "true")
    @NotNull(message = "흡연 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private boolean isSmoking;

    @ApiModelProperty(position = 8, required = true, example = "true")
    @NotNull(message = "실내 음식 섭취 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private boolean isAllowedFood;

    @ApiModelProperty(position = 9, required = true, example = "true")
    @NotNull(message = "이어폰 착용 여부 입력은 필수입니다.", groups = ValidationSequence.NotNull.class)
    private boolean isWearEarphones;

    @ApiModelProperty(position = 10, required = true, example = "오전 9시에 기상합니다.")
    @NotBlank(message = "기상 시간 입력은 필수입니다.", groups = ValidationSequence.NotBlank.class)
    private String wakeUpTime;

    @ApiModelProperty(position = 11, required = true, example = "밤 10시에 늘 청소해요.")
    @NotBlank(message = "정리정돈 상태 입력은 필수입니다.", groups = ValidationSequence.NotBlank.class)
    private String cleanUpStatus;

    @ApiModelProperty(position = 12, required = true, example = "아침 8시, 밤 12시에 주로 씻어요.")
    @NotBlank(message = "샤워 시간 입력은 필수입니다.", groups = ValidationSequence.NotBlank.class)
    private String showerTime;

    @ApiModelProperty(position = 13, required = true, example = "https://open.kakao.com/o/szaIN6ze")
    @NotBlank(message = "오픈 채팅 링크 입력은 필수입니다.", groups = ValidationSequence.NotBlank.class)
    private String openKakaoLink;

    @ApiModelProperty(position = 14, example = "ISTP")
    @Size(min = 3, max = 5, message = "mbti는 3~5자로 입력해 주세요.", groups = ValidationSequence.Size.class)
    private String mbti;

    @ApiModelProperty(position = 15, example = "적당한 선을 지키면서 친해질 수 있는 룸메이트 구해요 :)")
    @Size(min = 1, max = 50, message = "하고싶은 말은 1~50자로 입력해 주세요.", groups = ValidationSequence.Size.class)
    private String wishText;
}
