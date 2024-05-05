package idorm.idormServer.matchingInfo.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.matchingInfo.application.port.out.DeleteMatchingInfoPort;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeleteMatchingInfoAdapter implements DeleteMatchingInfoPort {

	private final MatchingInfoRepository matchingInfoRepository;

	@Override
	public void delete(Long matchingInfoId) {
		matchingInfoRepository.deleteById(matchingInfoId);
	}
}
