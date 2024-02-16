package idorm.idormServer.auth.service;

import idorm.idormServer.auth.domain.RefreshToken;
import idorm.idormServer.auth.repository.RefreshTokenRepository;
import idorm.idormServer.common.exception.CustomException;
import idorm.idormServer.common.exception.ExceptionCode;
import idorm.idormServer.support.token.TokenManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenManager tokenManager;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, TokenManager tokenManager) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenManager = tokenManager;
    }

    @Transactional
    public void saveToken(String token, Long memberId) {
        deleteToken(memberId);
        RefreshToken refreshToken = new RefreshToken(memberId, token);
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void matches(String refreshToken, Long memberId) {
        RefreshToken savedToken = refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(null, ExceptionCode.UNAUTHORIZED_REFRESH_MEMBER));

        if (!tokenManager.isValid(savedToken.getToken())) {
            refreshTokenRepository.delete(savedToken);
            throw new CustomException(null, ExceptionCode.UNAUTHORIZED_REFRESH_MEMBER);
        }
        savedToken.validateSameToken(refreshToken);
    }

    @Transactional
    public void deleteToken(Long memberId) {
        refreshTokenRepository.deleteAllByMemberId(memberId);
    }
}
