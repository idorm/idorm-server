package idorm.idormServer.matchingInfo.application.port.out;

import java.util.List;
import java.util.Optional;

import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateFilterRequest;

public interface LoadMatchingInfoPort {

	void validateNotExistence(Long memberId);

	MatchingInfo load(Long memberId);

	Optional<MatchingInfo> loadWithOptional(Long memberId);

	List<MatchingInfo> loadByBasicConditions(MatchingInfo matchingInfo);

	List<MatchingInfo> loadBySpecialConditions(MatchingInfo matchingInfo, MatchingMateFilterRequest request);
}
