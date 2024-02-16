package idorm.idormServer.auth.adapter.out.persistence;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.auth.application.port.out.SaveRefreshTokenPort;
import idorm.idormServer.auth.domain.RefreshToken;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SaveRefreshTokenAdaptor implements SaveRefreshTokenPort {

	private final RefreshTokenRepository refreshTokenRepository;
	private final RefreshTokenMapper refreshTokenMapper;

	@Override
	@Transactional
	public void save(final RefreshToken refreshToken) {
		refreshTokenRepository.save(refreshTokenMapper.toEntity(refreshToken));
	}
}
