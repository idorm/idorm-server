package idorm.idormServer.auth.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.auth.domain.RefreshToken;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenMapper {

	public RefreshTokenJpaEntity toEntity(RefreshToken refreshToken) {
		return new RefreshTokenJpaEntity(refreshToken.getId(), refreshToken.getMemberId(), refreshToken.getToken());
	}

	public RefreshToken toDomain(RefreshTokenJpaEntity entity) {
		return RefreshToken.forMapper(entity.getId(), entity.getMemberId(), entity.getToken());
	}
}
