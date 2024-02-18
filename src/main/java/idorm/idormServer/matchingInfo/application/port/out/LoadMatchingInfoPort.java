package idorm.idormServer.matchingInfo.application.port.out;

import java.util.List;

import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateFilterRequest;

public interface LoadMatchingInfoPort {

	void validateNotExistence(Long memberId);

	MatchingInfo load(Long memberId);

	List<MatchingInfo> loadByBasicConditions(MatchingInfo matchingInfo);

	List<MatchingInfo> loadBySpecialConditions(MatchingInfo matchingInfo, MatchingMateFilterRequest request);
}
