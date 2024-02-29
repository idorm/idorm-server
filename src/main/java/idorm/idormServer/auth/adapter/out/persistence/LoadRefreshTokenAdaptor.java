package idorm.idormServer.auth.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.auth.adapter.out.exception.UnAuthorizedRefreshTokenException;
import idorm.idormServer.auth.application.port.out.LoadRefreshTokenPort;
import idorm.idormServer.auth.entity.RefreshToken;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoadRefreshTokenAdaptor implements LoadRefreshTokenPort {

	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public RefreshToken load(Long memberId) {

		return refreshTokenRepository.findByMemberId(memberId)
			.orElseThrow(UnAuthorizedRefreshTokenException::new);
	}
}
