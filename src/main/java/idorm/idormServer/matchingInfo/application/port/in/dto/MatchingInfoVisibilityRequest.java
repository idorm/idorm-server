package idorm.idormServer.matchingInfo.application.port.in.dto;

import javax.validation.constraints.NotNull;

public record MatchingInfoVisibilityRequest(
	@NotNull(message = "매칭 이미지 공개 여부는 공백일 수 없습니다.")
	Boolean isMatchingInfoPublic
) {
}
