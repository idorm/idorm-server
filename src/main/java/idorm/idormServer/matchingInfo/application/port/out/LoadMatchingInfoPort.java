package idorm.idormServer.matchingInfo.application.port.out;

import idorm.idormServer.matchingInfo.domain.MatchingInfo;

public interface LoadMatchingInfoPort {

	void validateNotExistence(Long memberId);

	MatchingInfo load(Long memberId);
}
