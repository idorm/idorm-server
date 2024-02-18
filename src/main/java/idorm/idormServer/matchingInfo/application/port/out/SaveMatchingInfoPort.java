package idorm.idormServer.matchingInfo.application.port.out;

import idorm.idormServer.matchingInfo.domain.MatchingInfo;

public interface SaveMatchingInfoPort {

	void save(MatchingInfo matchingInfo);
}
