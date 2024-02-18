package idorm.idormServer.matchingInfo.application.port.in;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.matchingInfo.application.port.in.dto.MatchingInfoRequest;
import idorm.idormServer.matchingInfo.application.port.in.dto.MatchingInfoResponse;
import idorm.idormServer.matchingInfo.application.port.in.dto.MatchingInfoVisibilityRequest;

public interface MatchingInfoUseCase {

	MatchingInfoResponse save(AuthResponse auth, MatchingInfoRequest request);

	MatchingInfoResponse editAll(AuthResponse auth, MatchingInfoRequest request);

	void editVisibility(AuthResponse auth, MatchingInfoVisibilityRequest request);

	MatchingInfoResponse getInfo(AuthResponse auth);

	void delete(AuthResponse auth);
}
