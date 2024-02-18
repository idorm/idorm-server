package idorm.idormServer.matchingMate.application.port.out;

import idorm.idormServer.matchingMate.domain.MatchingMate;

public interface SaveMatchingMatePort {

	void save(MatchingMate matchingMate);
}
