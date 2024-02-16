package idorm.idormServer.auth.application.port.out;

import idorm.idormServer.auth.domain.RefreshToken;

public interface LoadRefreshTokenPort {

	RefreshToken load(Long memberId);
}
