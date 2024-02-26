package idorm.idormServer.auth.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.auth.application.port.out.SaveRefreshTokenPort;
import idorm.idormServer.auth.entity.RefreshToken;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SaveRefreshTokenAdaptor implements SaveRefreshTokenPort {

	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public void save(final RefreshToken refreshToken) {
		refreshTokenRepository.save(refreshToken);
	}
}
