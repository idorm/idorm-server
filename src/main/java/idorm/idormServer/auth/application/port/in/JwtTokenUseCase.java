package idorm.idormServer.auth.application.port.in;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;

public interface JwtTokenUseCase {
	String createAccessToken(AuthResponse authInfo);

	String createRefreshToken();

	AuthResponse getParsedClaims(String token);

	boolean isValid(String token);
}