package idorm.idormServer.notification.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.notification.application.port.in.FcmTokenUseCase;
import idorm.idormServer.notification.application.port.in.dto.RegisterTokenRequest;
import idorm.idormServer.notification.application.port.out.LoadFcmTokenPort;
import idorm.idormServer.notification.application.port.out.SaveFcmTokenPort;
import idorm.idormServer.notification.domain.FcmToken;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FcmTokenService implements FcmTokenUseCase {

	private final LoadFcmTokenPort loadFcmTokenPort;
	private final SaveFcmTokenPort saveFcmTokenPort;

	@Override
	@Transactional
	public void save(final RegisterTokenRequest event) {
		loadFcmTokenPort.load(event.memberId())
			.ifPresentOrElse(fcmToken -> fcmToken.updateToken(event.fcmToken()),
				() -> saveFcmTokenPort.save(new FcmToken(event.memberId(), event.fcmToken())));
	}
}
