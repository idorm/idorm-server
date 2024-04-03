package idorm.idormServer.matchingInfo.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateFilterRequest;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateResponse;
import idorm.idormServer.matchingMate.entity.MatePreferenceType;
import idorm.idormServer.member.entity.Member;

@Repository
public interface MatchingInfoRepositoryCustom {

	List<MatchingMateResponse> findByMatePreference(Member member, MatePreferenceType preferenceType);

	List<MatchingMateResponse> findByBasicConditions(Member member);

	List<MatchingMateResponse> findBySpecialConditions(Member member, MatchingMateFilterRequest request);
}
