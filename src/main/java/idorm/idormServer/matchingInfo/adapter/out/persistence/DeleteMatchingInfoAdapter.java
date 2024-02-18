package idorm.idormServer.matchingInfo.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.matchingInfo.application.port.out.DeleteMatchingInfoPort;
import idorm.idormServer.matchingInfo.domain.MatchingInfo;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeleteMatchingInfoAdapter implements DeleteMatchingInfoPort {

	private MatchingInfoRepository matchingInfoRepository;
	private MatchingInfoMapper matchingInfoMapper;

	@Override
	public void delete(final MatchingInfo matchingInfo) {
		matchingInfoRepository.delete(matchingInfoMapper.toEntity(matchingInfo));
	}
}
