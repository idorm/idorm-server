package idorm.idormServer.auth.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.auth.application.port.out.DeleteRefreshTokenPort;
import idorm.idormServer.auth.domain.RefreshToken;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeleteRefreshTokenAdaptor implements DeleteRefreshTokenPort {

	private final RefreshTokenRepository refreshTokenRepository;
	private final RefreshTokenMapper refreshTokenMapper;

	@Override
	public void deleteAll(final Long memberId) {
		refreshTokenRepository.deleteAllByMemberId(memberId);
	}

	@Override
	public void delete(final RefreshToken refreshToken) {
		refreshTokenRepository.delete(refreshTokenMapper.toEntity(refreshToken));
	}
}
