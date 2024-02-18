package idorm.idormServer.matchingInfo.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.matchingInfo.application.port.out.SaveMatchingInfoPort;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SaveMatchingInfoAdapter implements SaveMatchingInfoPort {

	private final MatchingInfoRepository matchingInfoRepository;
	private final MatchingInfoMapper matchingInfoMapper;

	@Override
	public void save(final MatchingInfo matchingInfo) {
		matchingInfoRepository.save(matchingInfoMapper.toEntity(matchingInfo));
	}
}
