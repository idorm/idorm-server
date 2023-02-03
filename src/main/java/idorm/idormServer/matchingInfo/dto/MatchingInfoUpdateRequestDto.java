package idorm.idormServer.matchingInfo.dto;

import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.matchingInfo.domain.Gender;
import idorm.idormServer.matchingInfo.domain.JoinPeriod;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ApiModel(value = "MatchingInfo 수정 요청")
public class MatchingInfoUpdateRequestDto {

    @ApiModelProperty(position = 1, required = true, value = "기숙사 분류: DORM1, DORM2, DORM3", example = "DORM1")
    @NotNull(message = "기숙사 분류를 입력해 주세요.")
    private DormCategory dormCategory;

    @ApiModelProperty(position = 2, required = true, value = "입사기간: WEEK16, WEEK24", example = "WEEK16")
    @NotNull(message = "입사기간을 입력해 주세요.")
    private JoinPeriod joinPeriod;

    @ApiModelProperty(position = 3, required = true, value = "성별: MALE, FEMALE", example = "MALE")
    @NotNull(message = "성별을 입력해주세요.")
    private Gender gender;

    @NotNull(message = "나이 입력은 필수입니다.")
    @ApiModelProperty(position = 4, required = true, dataType = "Integer", example = "20")
    private Integer age;

    @NotNull(message = "코골이 여부 입력은 필수입니다.")
    @ApiModelProperty(position = 5, required = true, dataType = "Boolean", example = "true")
    private Boolean isSnoring;

    @NotNull(message = "이갈이 여부 입력은 필수입니다.")
    @ApiModelProperty(position = 6, required = true, dataType = "Boolean", example = "true")
    private Boolean isGrinding;

    @NotNull(message = "흡연 여부 입력은 필수입니다.")
    @ApiModelProperty(position = 7, required = true, dataType = "Boolean", example = "true")
    private Boolean isSmoking;

    @NotNull(message = "실내 음식 섭취 여부 입력은 필수입니다.")
    @ApiModelProperty(position = 8, required = true, dataType = "Boolean", example = "true")
    private Boolean isAllowedFood;

    @NotNull(message = "이어폰 착용 여부 입력은 필수입니다.")
    @ApiModelProperty(position = 9, required = true, dataType = "Boolean", example = "true")
    private Boolean isWearEarphones;

    @NotNull(message = "기상 시간 입력은 필수입니다.")
    @ApiModelProperty(position = 10, required = true, dataType = "String", example = "오전 9시에 기상합니다.")
    private String wakeUpTime;

    @NotNull(message = "정리정돈 상태 입력은 필수입니다.")
    @ApiModelProperty(position = 11, required = true, dataType = "String", example = "밤 10시에 늘 청소해요.")
    private String cleanUpStatus;

    @NotNull(message = "샤워 시간 입력은 필수입니다.")
    @ApiModelProperty(position = 12, required = true, dataType = "String", example = "아침 8시, 밤 12시에 주로 씻어요.")
    private String showerTime;

    @NotBlank(message = "오픈 채팅 링크 입력은 필수입니다.")
    @ApiModelProperty(position = 13, required = true, dataType = "String", example = "https://open.kakao.com/o/szaIN6ze")
    private String openKakaoLink;

    @ApiModelProperty(position = 14, dataType = "String", example = "ISTP")
    private String mbti;

    @ApiModelProperty(position = 15, dataType = "String", example = "적당한 선을 지키면서 친해질 수 있는 룸메이트 구해요 :)")
    private String wishText;
}
