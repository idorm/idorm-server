package idorm.idormServer.auth.application.port.in;

public interface RefreshTokenUseCase {

	void saveToken(String token, Long memberId);

	void matches(String refreshToken, Long memberId);

	void deleteToken(Long memberId);
}
