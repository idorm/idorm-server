package idorm.idormServer.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.auth.adapter.out.api.exception.UnAuthorizedRefreshTokenException;
import idorm.idormServer.auth.application.port.in.JwtTokenUseCase;
import idorm.idormServer.auth.application.port.in.RefreshTokenUseCase;
import idorm.idormServer.auth.application.port.out.DeleteRefreshTokenPort;
import idorm.idormServer.auth.application.port.out.LoadRefreshTokenPort;
import idorm.idormServer.auth.application.port.out.SaveRefreshTokenPort;
import idorm.idormServer.auth.entity.RefreshToken;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenUseCase {

	private final JwtTokenUseCase jwtTokenUseCase;
	private final SaveRefreshTokenPort saveRefreshTokenPort;
	private final LoadRefreshTokenPort loadRefreshTokenPort;
	private final DeleteRefreshTokenPort deleteRefreshTokenPort;

	@Override
	@Transactional
	public void saveToken(String token, Long memberId) {
		expire(memberId);
		RefreshToken refreshToken = new RefreshToken(memberId, token);
		saveRefreshTokenPort.save(refreshToken);
	}

	@Override
	@Transactional
	public void matches(String refreshToken, Long memberId) {

		RefreshToken savedToken = loadRefreshTokenPort.load(memberId);

		if (!jwtTokenUseCase.isValid(savedToken.getToken())) {
			deleteRefreshTokenPort.delete(savedToken);
			throw new UnAuthorizedRefreshTokenException();
		}
		savedToken.validateSameToken(refreshToken);
	}

	@Override
	@Transactional
	public void expire(Long memberId) {
		deleteRefreshTokenPort.deleteAll(memberId);
	}
}
