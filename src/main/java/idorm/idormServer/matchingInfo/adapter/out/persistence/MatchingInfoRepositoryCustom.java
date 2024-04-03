package idorm.idormServer.matchingInfo.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateFilterRequest;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateResponse;
import idorm.idormServer.matchingMate.entity.MatePreferenceType;
import idorm.idormServer.member.entity.Member;

@Repository
public interface MatchingInfoRepositoryCustom {

	Page<MatchingMateResponse> findByMatePreference(Member member, MatePreferenceType preferenceType,
		Pageable pageable);

	Page<MatchingMateResponse> findByBasicConditions(Member member, Pageable pageable);

	Page<MatchingMateResponse> findBySpecialConditions(Member member, MatchingMateFilterRequest request,
		Pageable pageable);
}
