package idorm.idormServer.matchingInfo.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.matchingInfo.application.port.out.DeleteMatchingInfoPort;
import idorm.idormServer.matchingInfo.entity.MatchingInfo;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeleteMatchingInfoAdapter implements DeleteMatchingInfoPort {

	private MatchingInfoRepository matchingInfoRepository;

	@Override
	public void delete(final MatchingInfo matchingInfo) {
		matchingInfoRepository.delete(matchingInfo);
	}
}
