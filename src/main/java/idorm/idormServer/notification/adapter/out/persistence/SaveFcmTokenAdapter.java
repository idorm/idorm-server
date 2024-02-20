package idorm.idormServer.notification.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.notification.application.port.out.SaveFcmTokenPort;
import idorm.idormServer.notification.domain.FcmToken;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SaveFcmTokenAdapter implements SaveFcmTokenPort {

	private final FcmTokenRepository fcmTokenRepository;
	private final FcmTokenMapper fcmTokenMapper;

	@Override
	public void save(final FcmToken fcmToken) {
		fcmTokenRepository.save(fcmTokenMapper.toEntity(fcmToken));
	}
}
