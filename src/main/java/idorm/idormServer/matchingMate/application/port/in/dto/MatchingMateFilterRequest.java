package idorm.idormServer.matchingMate.application.port.in.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

public record MatchingMateFilterRequest(
	@NotBlank(message = "기숙사 분류를 입력해 주세요.")
	String dormCategory,

	@NotBlank(message = "입사기간을 입력해 주세요.")
	String joinPeriod,

	@NotNull(message = "매칭을 원하는 룸메이트의 최소 나이 입력은 필수입니다.")
	@Range(min = 20, max = 100, message = "age, 나이는 20~100살 이내여야 합니다.")
	Integer minAge,

	@NotNull(message = "매칭을 원하는 룸메이트의 최대 나이 입력은 필수입니다.")
	@Range(min = 20, max = 100, message = "age, 나이는 20~100살 이내여야 합니다.")
	Integer maxAge,

	@NotNull(message = "코골이 여부 입력은 필수입니다.")
	Boolean isSnoring,

	@NotNull(message = "이갈이 여부 입력은 필수입니다.")
	Boolean isGrinding,

	@NotNull(message = "흡연 여부 입력은 필수입니다.")
	Boolean isSmoking,

	@NotNull(message = "실내 음식 허용 여부 입력은 필수입니다.")
	Boolean isAllowedFood,

	@NotNull(message = "이어폰 착용 의사 여부 입력은 필수입니다.")
	Boolean isWearEarphones
) {
}
