package idorm.idormServer.matchingInfo.adapter.out.persistence;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.matchingInfo.adapter.out.exception.DuplicatedMatchingInfoException;
import idorm.idormServer.matchingInfo.adapter.out.exception.NotFoundMatchingInfoException;
import idorm.idormServer.matchingInfo.application.port.out.LoadMatchingInfoPort;
import idorm.idormServer.matchingInfo.domain.DormCategory;
import idorm.idormServer.matchingInfo.domain.JoinPeriod;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateFilterRequest;
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

	@Override
	public List<MatchingInfo> loadByBasicConditions(final MatchingInfo matchingInfo) {

		MatchingInfoJpaEntity entity = matchingInfoMapper.toEntity(matchingInfo);

		List<MatchingInfoJpaEntity> results = matchingInfoRepository.findAllByMemberIdNotAndDormCategoryAndJoinPeriodAndGenderAndIsMatchingInfoPublicTrue(
			matchingInfo.getMember().getId(),
			entity.getDormInfo().getDormCategory(),
			entity.getDormInfo().getJoinPeriod(),
			entity.getDormInfo().getGender());

		return results.isEmpty() ? new ArrayList<>() : matchingInfoMapper.toDomain(results);
	}

	@Override
	public List<MatchingInfo> loadBySpecialConditions(final MatchingInfo matchingInfo,
		final MatchingMateFilterRequest request) {

		MatchingInfoJpaEntity entity = matchingInfoMapper.toEntity(matchingInfo);

		List<MatchingInfoJpaEntity> results = matchingInfoRepository.findFilteredMates(
			matchingInfo.getMember().getId(),
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

		return results.isEmpty() ? new ArrayList<>() : matchingInfoMapper.toDomain(results);
	}
}
