package idorm.idormServer.matchingInfo.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import idorm.idormServer.matchingInfo.entity.MatchingInfo;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateFilterRequest;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateResponse;
import idorm.idormServer.matchingMate.entity.MatePreferenceType;
import idorm.idormServer.member.entity.Member;

public interface LoadMatchingInfoPort {

	void validateNotExistence(Long memberId);

	MatchingInfo load(Long memberId);

	Page<MatchingMateResponse> loadByMatePreference(Member member, MatePreferenceType preferenceType,
		Pageable pageable);

	Page<MatchingMateResponse> loadByBasicConditions(Member member, Pageable pageable);

	Page<MatchingMateResponse> loadBySpecialConditions(Member member, MatchingMateFilterRequest request,
		Pageable pageable);
}
