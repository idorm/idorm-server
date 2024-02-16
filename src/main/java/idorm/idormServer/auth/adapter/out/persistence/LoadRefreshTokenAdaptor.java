package idorm.idormServer.auth.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.auth.adapter.out.api.exception.UnAuthorizedRefreshTokenException;
import idorm.idormServer.auth.application.port.out.LoadRefreshTokenPort;
import idorm.idormServer.auth.domain.RefreshToken;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoadRefreshTokenAdaptor implements LoadRefreshTokenPort {

	private final RefreshTokenMapper refreshTokenMapper;
	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public RefreshToken load(Long memberId) {

		RefreshTokenJpaEntity refreshTokenJpaEntity = refreshTokenRepository.findByMemberId(memberId)
			.orElseThrow(UnAuthorizedRefreshTokenException::new);

		return refreshTokenMapper.toDomain(refreshTokenJpaEntity);
	}
}
