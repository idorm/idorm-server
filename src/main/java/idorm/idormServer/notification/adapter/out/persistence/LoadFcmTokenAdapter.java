package idorm.idormServer.notification.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import idorm.idormServer.notification.application.port.out.LoadFcmTokenPort;
import idorm.idormServer.notification.domain.FcmToken;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoadFcmTokenAdapter implements LoadFcmTokenPort {

	private final FcmTokenRepository fcmTokenRepository;
	private final FcmTokenMapper fcmTokenMapper;

	@Override
	public Optional<FcmToken> load(Long memberId) {
		Optional<FcmTokenJpaEntity> token = fcmTokenRepository.findByMemberId(memberId);
		return Optional.ofNullable(token.map(fcmTokenMapper::toDomain).orElse(null));
	}
}
