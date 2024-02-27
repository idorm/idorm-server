package idorm.idormServer.matchingMate.application.port.in.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import idorm.idormServer.matchingMate.entity.MatePreferenceType;

public record PreferenceMateRequest(
	@NotBlank(message = "메이트 선호도 형식은 공백일 수 없습니다.")
	String preferenceType,

	@NotNull(message = "대상 회원 식별자는 공백일 수 없습니다.")
	@Positive(message = "대상 회원 식별자는 양수만 가능합니다.")
	Long targetMemberId
) {
	public MatePreferenceType getPreferenceType() {
		return MatePreferenceType.from(preferenceType);
	}
}
