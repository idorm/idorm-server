package idorm.idormServer.matchingMate.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.matchingMate.application.port.out.SaveMatchingMatePort;
import idorm.idormServer.matchingMate.entity.MatchingMate;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SaveMatchingMateAdapter implements SaveMatchingMatePort {

	private final MatchingMateRepository matchingMateRepository;

	@Override
	public void save(final MatchingMate matchingMate) {
		matchingMateRepository.save(matchingMate);
	}
}