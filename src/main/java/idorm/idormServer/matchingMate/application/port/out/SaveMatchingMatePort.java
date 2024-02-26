package idorm.idormServer.matchingMate.application.port.out;

import idorm.idormServer.matchingMate.entity.MatchingMate;

public interface SaveMatchingMatePort {

	void save(MatchingMate matchingMate);
}
