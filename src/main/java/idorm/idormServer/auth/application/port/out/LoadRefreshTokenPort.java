package idorm.idormServer.auth.application.port.out;

import idorm.idormServer.auth.entity.RefreshToken;

public interface LoadRefreshTokenPort {

	RefreshToken load(Long memberId);
}
