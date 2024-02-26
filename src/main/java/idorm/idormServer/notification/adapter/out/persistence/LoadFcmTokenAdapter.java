package idorm.idormServer.notification.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import idorm.idormServer.notification.application.port.out.LoadFcmTokenPort;
import idorm.idormServer.notification.entity.FcmToken;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoadFcmTokenAdapter implements LoadFcmTokenPort {

	private final FcmTokenRepository fcmTokenRepository;

	@Override
	public Optional<FcmToken> load(Long memberId) {
		return fcmTokenRepository.findByMemberId(memberId);
	}
}
