package idorm.idormServer.matchingInfo.adapter.out.persistence;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.matchingInfo.adapter.out.exception.DuplicatedMatchingInfoException;
import idorm.idormServer.matchingInfo.adapter.out.exception.NotFoundMatchingInfoException;
import idorm.idormServer.matchingInfo.application.port.out.LoadMatchingInfoPort;
import idorm.idormServer.matchingInfo.entity.DormCategory;
import idorm.idormServer.matchingInfo.entity.JoinPeriod;
import idorm.idormServer.matchingInfo.entity.MatchingInfo;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateFilterRequest;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateResponse;
import idorm.idormServer.matchingMate.entity.MatePreferenceType;
import idorm.idormServer.member.entity.Member;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoadMatchingInfoAdapter implements LoadMatchingInfoPort {

	private final MatchingInfoRepository matchingInfoRepository;

	@Override
	public void validateNotExistence(final Long memberId) {
		final boolean exists = matchingInfoRepository.existsByMemberId(memberId);
		if (exists) {
			throw new DuplicatedMatchingInfoException();
		}
	}

	@Override
	public MatchingInfo load(final Long memberId) {
		return matchingInfoRepository.findByMemberId(memberId)
			.orElseThrow(NotFoundMatchingInfoException::new);
	}

	@Override
	public List<MatchingMateResponse> loadByMatePreference(Member member, MatePreferenceType preferenceType) {
		List<MatchingMateResponse> responses = matchingInfoRepository.findByMatePreference(member, preferenceType);
		return responses != null ? responses : Collections.emptyList();
	}

	@Override
	public List<MatchingMateResponse> loadByBasicConditions(final Member member) {
		final List<MatchingMateResponse> responses = matchingInfoRepository.findByBasicConditions(member);
		return responses != null ? responses : Collections.emptyList();
	}

	@Override
	public List<MatchingMateResponse> loadBySpecialConditions(final Member member,
		final MatchingMateFilterRequest request) {

		final List<MatchingMateResponse> responses = matchingInfoRepository.findBySpecialConditions(member, request);
		return responses != null ? responses : Collections.emptyList();
	}
}