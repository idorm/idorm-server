package idorm.idormServer.matchingInfo.application.port.out;

import idorm.idormServer.matchingInfo.entity.MatchingInfo;

public interface SaveMatchingInfoPort {

	void save(MatchingInfo matchingInfo);
}
