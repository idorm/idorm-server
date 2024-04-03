package idorm.idormServer.matchingMate.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateFilterRequest;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateResponse;
import idorm.idormServer.matchingMate.application.port.in.dto.PreferenceMateRequest;

public interface MatchingMateUseCase {

	Page<MatchingMateResponse> findFavoriteMates(AuthResponse auth, Pageable pageable);

	Page<MatchingMateResponse> findNonFavoriteMates(AuthResponse auth, Pageable pageable);

	void addMate(AuthResponse auth, PreferenceMateRequest request);

	void deleteMate(AuthResponse auth, PreferenceMateRequest request);

	Page<MatchingMateResponse> findMates(AuthResponse auth, Pageable pageable);

	Page<MatchingMateResponse> findFilteredMates(AuthResponse auth, MatchingMateFilterRequest request,
		Pageable pageable);
}
