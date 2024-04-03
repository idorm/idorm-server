package idorm.idormServer.matchingInfo.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import idorm.idormServer.matchingInfo.adapter.out.exception.DuplicatedMatchingInfoException;
import idorm.idormServer.matchingInfo.adapter.out.exception.NotFoundMatchingInfoException;
import idorm.idormServer.matchingInfo.application.port.out.LoadMatchingInfoPort;
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
	public Page<MatchingMateResponse> loadByMatePreference(final Member member, final MatePreferenceType preferenceType,
		final Pageable pageable) {
		return matchingInfoRepository.findByMatePreference(member, preferenceType, pageable);
	}

	@Override
	public Page<MatchingMateResponse> loadByBasicConditions(final Member member, final Pageable pageable) {
		return matchingInfoRepository.findByBasicConditions(member, pageable);
	}

	@Override
	public Page<MatchingMateResponse> loadBySpecialConditions(final Member member,
		final MatchingMateFilterRequest request, final Pageable pageable) {

		return matchingInfoRepository.findBySpecialConditions(member, request, pageable);
	}
}