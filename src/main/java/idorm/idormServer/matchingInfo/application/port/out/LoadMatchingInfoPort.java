package idorm.idormServer.matchingInfo.application.port.out;

import java.util.List;

import idorm.idormServer.matchingInfo.entity.MatchingInfo;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateFilterRequest;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateResponse;
import idorm.idormServer.matchingMate.entity.MatePreferenceType;
import idorm.idormServer.member.entity.Member;

public interface LoadMatchingInfoPort {

	void validateNotExistence(Long memberId);

	MatchingInfo load(Long memberId);

	List<MatchingMateResponse> loadByMatePreference(Member member, MatePreferenceType preferenceType);

	List<MatchingInfo> loadByBasicConditions(MatchingInfo matchingInfo);

	List<MatchingInfo> loadBySpecialConditions(MatchingInfo matchingInfo, MatchingMateFilterRequest request);
}
