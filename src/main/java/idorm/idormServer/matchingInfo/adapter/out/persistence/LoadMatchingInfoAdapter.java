package idorm.idormServer.matchingInfo.adapter.out.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import idorm.idormServer.matchingInfo.adapter.out.exception.DuplicatedMatchingInfoException;
import idorm.idormServer.matchingInfo.adapter.out.exception.NotFoundMatchingInfoException;
import idorm.idormServer.matchingInfo.application.port.out.LoadMatchingInfoPort;
import idorm.idormServer.matchingInfo.entity.DormCategory;
import idorm.idormServer.matchingInfo.entity.JoinPeriod;
import idorm.idormServer.matchingInfo.entity.MatchingInfo;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateFilterRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoadMatchingInfoAdapter implements LoadMatchingInfoPort {

	private final MatchingInfoRepository matchingInfoRepository;

	@Override
	public void validateNotExistence(final Long memberId) {
		boolean exists = matchingInfoRepository.existsByMemberId(memberId);

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
	public Optional<MatchingInfo> loadWithOptional(Long memberId) {
		return matchingInfoRepository.findByMemberId(memberId);
	}

	@Override
	public List<MatchingInfo> loadByBasicConditions(final MatchingInfo matchingInfo) {

		final List<MatchingInfo> results = matchingInfoRepository.findAllByMemberIdNotAndDormCategoryAndJoinPeriodAndGenderAndIsMatchingInfoPublicTrue(
			matchingInfo.getMember().getId(),
			matchingInfo.getDormInfo().getDormCategory(),
			matchingInfo.getDormInfo().getJoinPeriod(),
			matchingInfo.getDormInfo().getGender());

		return results.isEmpty() ? new ArrayList<>() : results;
	}

	@Override
	public List<MatchingInfo> loadBySpecialConditions(final MatchingInfo matchingInfo,
		final MatchingMateFilterRequest request) {

		final List<MatchingInfo> results = matchingInfoRepository.findFilteredMates(
			matchingInfo.getId(),
			DormCategory.from(request.dormCategory()),
			JoinPeriod.from(request.joinPeriod()),
			matchingInfo.getDormInfo().getGender(),
			!request.isSnoring(),
			!request.isSmoking(),
			!request.isGrinding(),
			!request.isWearEarphones(),
			!request.isAllowedFood(),
			request.minAge(),
			request.maxAge());

		return results.isEmpty() ? new ArrayList<>() : results;
	}
}