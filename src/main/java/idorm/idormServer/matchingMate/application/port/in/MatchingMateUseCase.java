package idorm.idormServer.matchingMate.application.port.in;

import java.util.List;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateFilterRequest;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateResponse;
import idorm.idormServer.matchingMate.application.port.in.dto.PreferenceMateRequest;

public interface MatchingMateUseCase {

	List<MatchingMateResponse> findFavoriteMates(AuthResponse auth);

	List<MatchingMateResponse> findNonFavoriteMates(AuthResponse auth);

	void addMate(AuthResponse auth, PreferenceMateRequest request);

	void deleteMate(AuthResponse auth, PreferenceMateRequest request);

	List<MatchingMateResponse> findMates(AuthResponse auth);

	List<MatchingMateResponse> findFilteredMates(AuthResponse auth, MatchingMateFilterRequest request);
}
