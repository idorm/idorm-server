package idorm.idormServer.matchingInfo.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.matchingInfo.adapter.out.exception.DuplicatedMatchingInfoException;
import idorm.idormServer.matchingInfo.adapter.out.exception.NotFoundMatchingInfoException;
import idorm.idormServer.matchingInfo.application.port.out.LoadMatchingInfoPort;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoadMatchingInfoAdapter implements LoadMatchingInfoPort {

	private final MatchingInfoRepository matchingInfoRepository;
	private final MatchingInfoMapper matchingInfoMapper;

	@Override
	public void validateNotExistence(final Long memberId) {
		boolean exists = matchingInfoRepository.existsByMemberId(memberId);

		if (exists) {
			throw new DuplicatedMatchingInfoException();
		}
	}

	@Override
	public MatchingInfo load(final Long memberId) {
		MatchingInfoJpaEntity matchingInfoJpaEntity = matchingInfoRepository.findByMemberId(memberId)
			.orElseThrow(NotFoundMatchingInfoException::new);

		return matchingInfoMapper.toDomain(matchingInfoJpaEntity);
	}
}
