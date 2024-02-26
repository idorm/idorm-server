package idorm.idormServer.auth.application.port.out;

import idorm.idormServer.auth.entity.RefreshToken;

public interface DeleteRefreshTokenPort {

	void deleteAll(Long memberId);

	void delete(RefreshToken refreshToken);
}
