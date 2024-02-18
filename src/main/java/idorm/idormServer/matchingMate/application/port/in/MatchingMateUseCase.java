package idorm.idormServer.matchingMate.application.port.in;

import java.util.List;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateFilterRequest;
import idorm.idormServer.matchingMate.application.port.in.dto.MatchingMateResponse;

public interface MatchingMateUseCase {

	List<MatchingMateResponse> findFavoriteMates(AuthResponse auth);

	List<MatchingMateResponse> findNonFavoriteMates(AuthResponse auth);

	void addFavoriteMate(AuthResponse auth, Long targetMemberId);

	void addNonFavoriteMate(AuthResponse auth, Long targetMemberId);

	void deleteFavoriteMate(AuthResponse auth, Long targetMemberId);

	void deleteNonFavoriteMate(AuthResponse auth, Long targetMemberId);

	List<MatchingMateResponse> findMates(AuthResponse auth);

	List<MatchingMateResponse> findFilteredMates(AuthResponse auth, MatchingMateFilterRequest request);
}
