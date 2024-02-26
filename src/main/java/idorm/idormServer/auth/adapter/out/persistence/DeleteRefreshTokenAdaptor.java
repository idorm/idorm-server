package idorm.idormServer.auth.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.auth.application.port.out.DeleteRefreshTokenPort;
import idorm.idormServer.auth.entity.RefreshToken;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeleteRefreshTokenAdaptor implements DeleteRefreshTokenPort {

	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public void deleteAll(final Long memberId) {
		refreshTokenRepository.deleteAllByMemberId(memberId);
	}

	@Override
	public void delete(final RefreshToken refreshToken) {
		refreshTokenRepository.delete(refreshToken);
	}
}
